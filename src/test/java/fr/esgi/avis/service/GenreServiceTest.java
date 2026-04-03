package fr.esgi.avis.service;

import fr.esgi.avis.business.Genre;
import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import fr.esgi.avis.port.out.GenrePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenreService Tests")
class GenreServiceTest {

    @Mock
    private GenrePort genrePort;

    @InjectMocks
    private GenreService genreService;

    private GenreDtoIn genreDtoIn;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genreDtoIn = new GenreDtoIn(null, "RPG");
        genre = new Genre(1L, "RPG", List.of());
    }

    @Test
    @DisplayName("Should create a new genre successfully")
    void testCreerUnGenre() {
        when(genrePort.save(any(Genre.class))).thenReturn(genre);

        GenreDtoOut result = genreService.creerUnGenre(genreDtoIn);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("RPG");
        verify(genrePort, times(1)).save(any(Genre.class));
    }

    @Test
    @DisplayName("Should update an existing genre")
    void testMettreAJourUnGenre() {
        GenreDtoIn updateDto = new GenreDtoIn(1L, "Action RPG");
        Genre updatedGenre = new Genre(1L, "Action RPG", List.of());

        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));
        when(genrePort.save(any(Genre.class))).thenReturn(updatedGenre);

        GenreDtoOut result = genreService.mettreAJourUnGenre(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("Action RPG");
        verify(genrePort, times(1)).findById(1L);
        verify(genrePort, times(1)).save(any(Genre.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating non-existent genre")
    void testMettreAJourUnGenreNotFound() {
        when(genrePort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.mettreAJourUnGenre(999L, genreDtoIn))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Genre non trouvé");
        verify(genrePort, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should retrieve genre by id")
    void testRecupererUnGenreParId() {
        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));

        GenreDtoOut result = genreService.recupererUnGenreParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.nom()).isEqualTo("RPG");
        verify(genrePort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when genre not found by id")
    void testRecupererUnGenreParIdNotFound() {
        when(genrePort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.recupererUnGenreParId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Genre non trouvé");
        verify(genrePort, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should retrieve all genres")
    void testRecupererTouslesGenre() {
        Genre genre2 = new Genre(2L, "Adventure", List.of());
        when(genrePort.findAll()).thenReturn(List.of(genre, genre2));

        List<GenreDtoOut> result = genreService.recupererTouslesGenre();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("nom").contains("RPG", "Adventure");
        verify(genrePort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no genres exist")
    void testRecupererTouslesGenreEmpty() {
        when(genrePort.findAll()).thenReturn(List.of());

        List<GenreDtoOut> result = genreService.recupererTouslesGenre();

        assertThat(result).isEmpty();
        verify(genrePort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete a genre")
    void testSupprimerUnGenre() {
        genreService.supprimerUnGenre(1L);

        verify(genrePort, times(1)).deleteById(1L);
    }
}

