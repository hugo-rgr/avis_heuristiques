package fr.esgi.avis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import fr.esgi.avis.port.in.GenreUseCase;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenreController Tests")
class GenreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenreUseCase genreUseCase;

    @InjectMocks
    private GenreController genreController;

    private ObjectMapper objectMapper;

    private GenreDtoIn genreDtoIn;
    private GenreDtoOut genreDtoOut;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
        objectMapper = TestObjectMappers.objectMapper();

        genreDtoIn = new GenreDtoIn(null, "RPG");
        genreDtoOut = new GenreDtoOut(1L, "RPG");
    }

    @Test
    @DisplayName("Should create a genre and return CREATED status")
    void testCreerUnGenre() throws Exception {
        when(genreUseCase.creerUnGenre(any(GenreDtoIn.class))).thenReturn(genreDtoOut);

        mockMvc.perform(post("/api/genres")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(genreDtoIn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("RPG"));

        verify(genreUseCase, times(1)).creerUnGenre(any(GenreDtoIn.class));
    }

    @Test
    @DisplayName("Should update a genre and return OK status")
    void testMettreAJourUnGenre() throws Exception {
        when(genreUseCase.mettreAJourUnGenre(eq(1L), any(GenreDtoIn.class))).thenReturn(genreDtoOut);

        mockMvc.perform(put("/api/genres/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(genreDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("RPG"));

        verify(genreUseCase, times(1)).mettreAJourUnGenre(eq(1L), any(GenreDtoIn.class));
    }

    @Test
    @DisplayName("Should retrieve genre by id and return OK status")
    void testRecupererUnGenreParId() throws Exception {
        when(genreUseCase.recupererUnGenreParId(1L)).thenReturn(genreDtoOut);

        mockMvc.perform(get("/api/genres/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("RPG"));

        verify(genreUseCase, times(1)).recupererUnGenreParId(1L);
    }

    @Test
    @DisplayName("Should retrieve all genres and return OK status")
    void testRecupererTouslesGenre() throws Exception {
        GenreDtoOut genre2 = new GenreDtoOut(2L, "Action");
        when(genreUseCase.recupererTouslesGenre()).thenReturn(List.of(genreDtoOut, genre2));

        mockMvc.perform(get("/api/genres")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value("RPG"))
                .andExpect(jsonPath("$[1].nom").value("Action"));

        verify(genreUseCase, times(1)).recupererTouslesGenre();
    }

    @Test
    @DisplayName("Should delete genre and return NO_CONTENT status")
    void testSupprimerUnGenre() throws Exception {
        doNothing().when(genreUseCase).supprimerUnGenre(1L);

        mockMvc.perform(delete("/api/genres/1")
                .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(genreUseCase, times(1)).supprimerUnGenre(1L);
    }
}
