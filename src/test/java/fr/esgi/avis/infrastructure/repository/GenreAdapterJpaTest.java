package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreAdapter.class)
@DisplayName("GenreAdapter @DataJpaTest")
class GenreAdapterJpaTest {

    @Autowired
    private GenreAdapter genreAdapter;

    @Test
    @DisplayName("save/findAll/findById/deleteById doivent fonctionner")
    void crud() {
        Genre saved = genreAdapter.save(new Genre(null, "RPG", List.of()));
        assertThat(saved.getId()).isNotNull();

        assertThat(genreAdapter.findAll())
                .extracting(Genre::getNom)
                .contains("RPG");

        assertThat(genreAdapter.findById(saved.getId())).isPresent();

        genreAdapter.deleteById(saved.getId());
        assertThat(genreAdapter.findById(saved.getId())).isEmpty();
    }
}

