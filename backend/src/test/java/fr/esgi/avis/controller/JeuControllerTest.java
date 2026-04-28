package fr.esgi.avis.controller;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.port.in.JeuUseCase;
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
@DisplayName("JeuController Tests")
class JeuControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JeuUseCase jeuUseCase;

    @InjectMocks
    private JeuController jeuController;

    private ObjectMapper objectMapper;

    private JeuDtoIn jeuDtoIn;
    private JeuDtoOut jeuDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jeuController).build();
        objectMapper = TestObjectMappers.objectMapper();

        jeuDtoIn = new JeuDtoIn(
                "Baldur's Gate 3",
                "Fantasy RPG",
                1L,
                "bg3.jpg",
                59.99f,
                LocalDate.of(2023, 8, 3),
                1L,
                1L,
                List.of()
        );
        jeuDtoOut = new JeuDtoOut(1L, "Baldur's Gate 3", "Fantasy RPG", "bg3.jpg",
                59.99f, LocalDate.of(2023, 8, 3), "RPG", "Larian Studios", "PEGI 16", List.of(), null);
    }

    @Test
    @DisplayName("Should create a game and return CREATED status")
    void testCreerUnJeu() throws Exception {
        when(jeuUseCase.creerUnJeu(any(JeuDtoIn.class))).thenReturn(jeuDtoOut);

        mockMvc.perform(post("/api/jeux")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jeuDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Baldur's Gate 3"))
                .andExpect(jsonPath("$.prix").value(59.99));

        verify(jeuUseCase, times(1)).creerUnJeu(any(JeuDtoIn.class));
    }

    @Test
    @DisplayName("Should update a game and return OK status")
    void testMettreAJourUnJeu() throws Exception {
        when(jeuUseCase.mettreAJourUnJeu(eq(1L), any(JeuDtoIn.class))).thenReturn(jeuDtoOut);

        mockMvc.perform(put("/api/jeux/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jeuDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Baldur's Gate 3"));

        verify(jeuUseCase, times(1)).mettreAJourUnJeu(eq(1L), any(JeuDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve game by id and return OK status")
    void testRecupererUnJeuParId() throws Exception {
        when(jeuUseCase.recupererUnJeuParId(1L)).thenReturn(jeuDtoOut);

        mockMvc.perform(get("/api/jeux/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Baldur's Gate 3"));

        verify(jeuUseCase, times(1)).recupererUnJeuParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all games and return OK status")
    void testRecupererTousLesJeux() throws Exception {
        JeuDtoOut jeu2 = new JeuDtoOut(2L, "Elden Ring", "Action RPG", "er.jpg",
                59.99f, LocalDate.of(2022, 2, 25), "Action", "FromSoftware", "PEGI 16", List.of(), null);
        when(jeuUseCase.recupererTousLesJeux()).thenReturn(List.of(jeuDtoOut, jeu2));

        mockMvc.perform(get("/api/jeux")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("Baldur's Gate 3"))
                .andExpect(jsonPath("$[1].nom").value("Elden Ring"));

        verify(jeuUseCase, times(1)).recupererTousLesJeux();
    }

    @Test
    @DisplayName("Should retrieve games by genre id and return OK status")
    void testRecupererDesJeuxDUnGenre() throws Exception {
        when(jeuUseCase.recupererDesJeuxDUnGenre(1L)).thenReturn(List.of(jeuDtoOut));

        mockMvc.perform(get("/api/jeux/genre/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].genreNom").value("RPG"));

        verify(jeuUseCase, times(1)).recupererDesJeuxDUnGenre(1L);
    }

    @Test
    @DisplayName("Should retrieve games by publisher id and return OK status")
    void testRecupererDesJeuxDUnEditeur() throws Exception {
        when(jeuUseCase.recupererDesJeuxDUnEditeur(1L)).thenReturn(List.of(jeuDtoOut));

        mockMvc.perform(get("/api/jeux/editeur/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].editeurNom").value("Larian Studios"));

        verify(jeuUseCase, times(1)).recupererDesJeuxDUnEditeur(1L);
    }

    @Test
    @DisplayName("Should delete game and return NO_CONTENT status")
    void testSupprimerUnJeu() throws Exception {
        doNothing().when(jeuUseCase).supprimerUnJeu(1L);

        mockMvc.perform(delete("/api/jeux/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(jeuUseCase, times(1)).supprimerUnJeu(1L);
    }
}
