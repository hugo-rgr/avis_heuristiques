package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.JeuJpaRepository;
import fr.esgi.avis.infrastructure.repository.PlateformeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("JeuEntity contraintes + relation M2M @DataJpaTest")
class JeuEntityJpaTest {

    @Autowired
    private JeuJpaRepository jeuJpaRepository;

    @Autowired
    private PlateformeJpaRepository plateformeJpaRepository;

    @Test
    @DisplayName("nom et prix sont obligatoires")
    void nomAndPrix_areRequired() {
        JeuEntity jeu = new JeuEntity();
        // nom + prix non set

        assertThatThrownBy(() -> jeuJpaRepository.saveAndFlush(jeu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("la relation ManyToMany jeu <-> plateformes se persiste")
    void manyToMany_plateformes_isPersisted() {
        PlateformeEntity p1 = new PlateformeEntity();
        p1.setNom("PC");
        p1 = plateformeJpaRepository.saveAndFlush(p1);

        PlateformeEntity p2 = new PlateformeEntity();
        p2.setNom("PS5");
        p2 = plateformeJpaRepository.saveAndFlush(p2);

        JeuEntity jeu = new JeuEntity();
        jeu.setNom("Game");
        jeu.setPrix(10f);
        jeu.setPlateformes(List.of(p1, p2));

        JeuEntity saved = jeuJpaRepository.saveAndFlush(jeu);

        JeuEntity reloaded = jeuJpaRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getPlateformes())
                .extracting(PlateformeEntity::getNom)
                .containsExactlyInAnyOrder("PC", "PS5");
    }
}

