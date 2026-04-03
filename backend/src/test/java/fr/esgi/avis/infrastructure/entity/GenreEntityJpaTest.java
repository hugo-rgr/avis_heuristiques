package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.GenreJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("GenreEntity contraintes @DataJpaTest")
class GenreEntityJpaTest {

    @Autowired
    private GenreJpaRepository genreJpaRepository;

    @Test
    @DisplayName("nom doit être unique")
    void nom_uniqueConstraint() {
        GenreEntity g1 = new GenreEntity();
        g1.setNom("RPG");
        genreJpaRepository.saveAndFlush(g1);

        GenreEntity g2 = new GenreEntity();
        g2.setNom("RPG");

        assertThatThrownBy(() -> genreJpaRepository.saveAndFlush(g2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

