package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Classification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ClassificationAdapter.class)
@DisplayName("ClassificationAdapter @DataJpaTest")
class ClassificationAdapterJpaTest {

    @Autowired
    private ClassificationAdapter classificationAdapter;

    @Test
    @DisplayName("save/findAll/findById/deleteById doivent fonctionner")
    void crud() {
        Classification saved = classificationAdapter.save(new Classification(null, "PEGI 12", List.of(), "#00FF00"));
        assertThat(saved.getId()).isNotNull();

        assertThat(classificationAdapter.findAll())
                .extracting(Classification::getNom)
                .contains("PEGI 12");

        assertThat(classificationAdapter.findById(saved.getId())).isPresent();

        classificationAdapter.deleteById(saved.getId());
        assertThat(classificationAdapter.findById(saved.getId())).isEmpty();
    }
}

