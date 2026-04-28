package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import fr.esgi.avis.port.in.JoueurUseCase;
import fr.esgi.avis.testutil.TestObjectMappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JoueurController Tests")
class JoueurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JoueurUseCase joueurUseCase;

    @InjectMocks
    private JoueurController joueurController;

    private ObjectMapper objectMapper;

    private JoueurDtoIn joueurDtoIn;
    private JoueurDtoOut joueurDtoOut;
    private AvisDtoIn avisDtoIn;
    private AvisDtoOut avisDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(joueurController).build();
        objectMapper = TestObjectMappers.objectMapper();

        joueurDtoIn = new JoueurDtoIn("gamer123", "gamer@example.com", "password123", LocalDate.of(1995, 5, 15));
        joueurDtoOut = new JoueurDtoOut(1L, "gamer123", "gamer@example.com", LocalDate.of(1995, 5, 15), null);
        avisDtoIn = new AvisDtoIn("Great game!", 1L, 1L, 9.5f, null, LocalDate.now());
        avisDtoOut = new AvisDtoOut(1L, "Great game!", 9.5f, "Game Name", "gamer123", null, LocalDate.now());
    }

    @Test
    @DisplayName("Should register a player and return CREATED status")
    void testSInscrire() throws Exception {
        when(joueurUseCase.sInscrire(any(JoueurDtoIn.class))).thenReturn(joueurDtoOut);

        mockMvc.perform(post("/api/joueurs/inscription")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(joueurDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pseudo").value("gamer123"))
                .andExpect(jsonPath("$.email").value("gamer@example.com"));

        verify(joueurUseCase, times(1)).sInscrire(any(JoueurDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve player by id and return OK status")
    void testTrouverParId() throws Exception {
        when(joueurUseCase.trouverParId(1L)).thenReturn(joueurDtoOut);

        mockMvc.perform(get("/api/joueurs/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pseudo").value("gamer123"));

        verify(joueurUseCase, times(1)).trouverParId(1L);
    }

    @Test
    @DisplayName("Should retrieve player's reviews and return OK status")
    void testListerAvisDuJoueur() throws Exception {
        AvisDtoOut avis2 = new AvisDtoOut(2L, "Awesome!", 9.0f, "Another Game", "gamer123", null, LocalDate.now());
        when(joueurUseCase.listerAvisDuJoueur(1L)).thenReturn(List.of(avisDtoOut, avis2));

        mockMvc.perform(get("/api/joueurs/1/avis")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("Great game!"))
                .andExpect(jsonPath("$[1].description").value("Awesome!"));

        verify(joueurUseCase, times(1)).listerAvisDuJoueur(1L);
    }

    @Test
    @DisplayName("Should write a review and return CREATED status")
    void testRedigerUnAvis() throws Exception {
        when(joueurUseCase.redigerUnAvis(eq(1L), any(AvisDtoIn.class))).thenReturn(avisDtoOut);

        mockMvc.perform(post("/api/joueurs/1/avis")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(avisDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Great game!"))
                .andExpect(jsonPath("$.note").value(9.5));

        verify(joueurUseCase, times(1)).redigerUnAvis(eq(1L), any(AvisDtoIn.class));
    }
}
