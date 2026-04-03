package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.EditeurJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("EditeurEntity contraintes @DataJpaTest")
class EditeurEntityJpaTest {

    @Autowired
    private EditeurJpaRepository editeurJpaRepository;

    @Test
    @DisplayName("nom doit être unique")
    void nom_uniqueConstraint() {
        EditeurEntity e1 = new EditeurEntity();
        e1.setNom("Nintendo");
        editeurJpaRepository.saveAndFlush(e1);

        EditeurEntity e2 = new EditeurEntity();
        e2.setNom("Nintendo");

        assertThatThrownBy(() -> editeurJpaRepository.saveAndFlush(e2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

