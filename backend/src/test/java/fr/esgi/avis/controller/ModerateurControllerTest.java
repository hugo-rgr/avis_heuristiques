package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.port.in.ModerateurUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModerateurController Tests")
class ModerateurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModerateurUseCase moderateurUseCase;

    @InjectMocks
    private ModerateurController moderateurController;

    private ObjectMapper objectMapper;

    private ModerateurDtoOut moderateurDtoOut;
    private JeuDtoIn jeuDtoIn;
    private JeuDtoOut jeuDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(moderateurController).build();
        objectMapper = TestObjectMappers.objectMapper();

        moderateurDtoOut = new ModerateurDtoOut(1L, "mod123", "mod@example.com", "0612345678");
        jeuDtoIn = new JeuDtoIn(
                null,
                "New Game",
                "Description",
                1L,
                "image.jpg",
                59.99f,
                LocalDate.now(),
                1L,
                1L,
                List.of()
        );
        jeuDtoOut = new JeuDtoOut(1L, "New Game", "Description", "image.jpg",
                59.99f, LocalDate.now(), "RPG", "Ubisoft", "PEGI 12", List.of());
    }

    @Test
    @DisplayName("Should find moderator by id and return OK status")
    void testTrouverParId() throws Exception {
        when(moderateurUseCase.trouverParId(1L)).thenReturn(moderateurDtoOut);

        mockMvc.perform(get("/api/moderateurs/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pseudo").value("mod123"));

        verify(moderateurUseCase, times(1)).trouverParId(1L);
    }

    @Test
    @DisplayName("Should add a game and return CREATED status")
    void testAjouterJeu() throws Exception {
        when(moderateurUseCase.ajouterJeu(eq(1L), any(JeuDtoIn.class))).thenReturn(jeuDtoOut);

        mockMvc.perform(post("/api/moderateurs/1/jeux")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jeuDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("New Game"));

        verify(moderateurUseCase, times(1)).ajouterJeu(eq(1L), any(JeuDtoIn.class));
    }

    @Test
    @DisplayName("Should delete a review and return NO_CONTENT status")
    void testSupprimerAvis() throws Exception {
        doNothing().when(moderateurUseCase).supprimerAvis(1L, 1L);

        mockMvc.perform(delete("/api/moderateurs/1/avis/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(moderateurUseCase, times(1)).supprimerAvis(1L, 1L);
    }
}
