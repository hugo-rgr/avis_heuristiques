package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.ClassificationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("ClassificationEntity contraintes @DataJpaTest")
class ClassificationEntityJpaTest {

    @Autowired
    private ClassificationJpaRepository classificationJpaRepository;

    @Test
    @DisplayName("nom doit être unique")
    void nom_uniqueConstraint() {
        ClassificationEntity c1 = new ClassificationEntity();
        c1.setNom("PEGI 12");
        c1.setCouleurRGB("#00FF00");
        classificationJpaRepository.saveAndFlush(c1);

        ClassificationEntity c2 = new ClassificationEntity();
        c2.setNom("PEGI 12");
        c2.setCouleurRGB("#FFFFFF");

        assertThatThrownBy(() -> classificationJpaRepository.saveAndFlush(c2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("couleurRGB est obligatoire")
    void couleurRgb_isRequired() {
        ClassificationEntity c = new ClassificationEntity();
        c.setNom("PEGI 7");
        // couleurRGB non set

        assertThatThrownBy(() -> classificationJpaRepository.saveAndFlush(c))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

