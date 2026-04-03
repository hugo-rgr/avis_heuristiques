package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Editeur;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(EditeurAdapter.class)
@DisplayName("EditeurAdapter @DataJpaTest")
class EditeurAdapterJpaTest {

    @Autowired
    private EditeurAdapter editeurAdapter;

    @Test
    @DisplayName("save/findAll/findById/deleteById doivent fonctionner")
    void crud() {
        Editeur saved = editeurAdapter.save(new Editeur(null, "Nintendo", List.of()));
        assertThat(saved.getId()).isNotNull();

        assertThat(editeurAdapter.findAll())
                .extracting(Editeur::getNom)
                .contains("Nintendo");

        assertThat(editeurAdapter.findById(saved.getId())).isPresent();

        editeurAdapter.deleteById(saved.getId());
        assertThat(editeurAdapter.findById(saved.getId())).isEmpty();
    }
}

