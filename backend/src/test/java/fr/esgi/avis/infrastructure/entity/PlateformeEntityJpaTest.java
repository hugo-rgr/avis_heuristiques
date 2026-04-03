package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.PlateformeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("PlateformeEntity contraintes @DataJpaTest")
class PlateformeEntityJpaTest {

    @Autowired
    private PlateformeJpaRepository plateformeJpaRepository;

    @Test
    @DisplayName("nom doit être unique")
    void nom_uniqueConstraint() {
        PlateformeEntity p1 = new PlateformeEntity();
        p1.setNom("PC");
        p1.setDateDeSortie(LocalDate.of(1990, 1, 1));
        plateformeJpaRepository.saveAndFlush(p1);

        PlateformeEntity p2 = new PlateformeEntity();
        p2.setNom("PC");
        p2.setDateDeSortie(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> plateformeJpaRepository.saveAndFlush(p2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

