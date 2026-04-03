package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.port.in.AvisUseCase;
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
@DisplayName("AvisController Tests")
class AvisControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvisUseCase avisUseCase;

    @InjectMocks
    private AvisController avisController;

    private ObjectMapper objectMapper;

    private AvisDtoIn avisDtoIn;
    private AvisDtoOut avisDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(avisController).build();
        objectMapper = TestObjectMappers.objectMapper();

        avisDtoIn = new AvisDtoIn(null, "Great game!", 1L, 1L, 9.5f, null, LocalDate.now());
        avisDtoOut = new AvisDtoOut(1L, "Great game!", 9.5f, "Game Name", "Player", null, LocalDate.now());
    }

    @Test
    @DisplayName("Should create a review and return CREATED status")
    void testCreerUnAvis() throws Exception {
        when(avisUseCase.creerUnAvis(any(AvisDtoIn.class))).thenReturn(avisDtoOut);

        mockMvc.perform(post("/api/avis")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(avisDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Great game!"))
                .andExpect(jsonPath("$.note").value(9.5));

        verify(avisUseCase, times(1)).creerUnAvis(any(AvisDtoIn.class));
    }

    @Test
    @DisplayName("Should update a review and return OK status")
    void testMettreAJourUnAvis() throws Exception {
        when(avisUseCase.mettreAJourUnAvis(eq(1L), any(AvisDtoIn.class))).thenReturn(avisDtoOut);

        mockMvc.perform(put("/api/avis/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(avisDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Great game!"));

        verify(avisUseCase, times(1)).mettreAJourUnAvis(eq(1L), any(AvisDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve review by id and return OK status")
    void testRecupererUnAvisParId() throws Exception {
        when(avisUseCase.recupererUnAvisParId(1L)).thenReturn(avisDtoOut);

        mockMvc.perform(get("/api/avis/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Great game!"));

        verify(avisUseCase, times(1)).recupererUnAvisParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all reviews and return OK status")
    void testRecupererTousLesAvis() throws Exception {
        AvisDtoOut avis2 = new AvisDtoOut(2L, "Awesome!", 9.0f, "Another Game", "Player2", null, LocalDate.now());
        when(avisUseCase.recupererTousLesAvis()).thenReturn(List.of(avisDtoOut, avis2));

        mockMvc.perform(get("/api/avis")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("Great game!"))
                .andExpect(jsonPath("$[1].description").value("Awesome!"));

        verify(avisUseCase, times(1)).recupererTousLesAvis();
    }

    @Test
    @DisplayName("Should retrieve reviews by game id and return OK status")
    void testRecupererTousLesAvisParJeu() throws Exception {
        when(avisUseCase.recupererTousLesAvisParJeu(1L)).thenReturn(List.of(avisDtoOut));

        mockMvc.perform(get("/api/avis/jeu/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(avisUseCase, times(1)).recupererTousLesAvisParJeu(1L);
    }

    @Test
    @DisplayName("Should retrieve reviews by player id and return OK status")
    void testRecupererTousLesAvisParJoueur() throws Exception {
        when(avisUseCase.recupererTousLesAvisParJoueur(1L)).thenReturn(List.of(avisDtoOut));

        mockMvc.perform(get("/api/avis/joueur/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].joueurPseudo").value("Player"));

        verify(avisUseCase, times(1)).recupererTousLesAvisParJoueur(1L);
    }

    @Test
    @DisplayName("Should delete review and return NO_CONTENT status")
    void testSupprimerUnAvis() throws Exception {
        doNothing().when(avisUseCase).supprimerUnAvis(1L);

        mockMvc.perform(delete("/api/avis/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(avisUseCase, times(1)).supprimerUnAvis(1L);
    }
}
