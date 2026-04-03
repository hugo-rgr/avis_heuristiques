package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Plateforme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PlateformeAdapter.class)
@DisplayName("PlateformeAdapter @DataJpaTest")
class PlateformeAdapterJpaTest {

    @Autowired
    private PlateformeAdapter plateformeAdapter;

    @Test
    @DisplayName("save/findAll/findById doivent fonctionner")
    void saveFindAllFindById() {
        Plateforme p = new Plateforme(null, "PC", List.of(), LocalDate.of(1990, 1, 1));
        Plateforme saved = plateformeAdapter.save(p);

        assertThat(saved.getId()).isNotNull();

        assertThat(plateformeAdapter.findAll())
                .extracting(Plateforme::getNom)
                .contains("PC");

        var found = plateformeAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDateDeSortie()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("findAllByIds doit filtrer")
    void findAllByIds_filters() {
        Plateforme p1 = plateformeAdapter.save(new Plateforme(null, "PS5", List.of(), LocalDate.of(2020, 11, 19)));

        List<Plateforme> found = plateformeAdapter.findAllByIds(List.of(p1.getId()));
        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getNom()).isEqualTo("PS5");
    }

    @Test
    @DisplayName("deleteById doit supprimer")
    void deleteById() {
        Plateforme p = plateformeAdapter.save(new Plateforme(null, "Switch", List.of(), LocalDate.of(2017, 3, 3)));
        plateformeAdapter.deleteById(p.getId());

        assertThat(plateformeAdapter.findById(p.getId())).isEmpty();
    }
}

