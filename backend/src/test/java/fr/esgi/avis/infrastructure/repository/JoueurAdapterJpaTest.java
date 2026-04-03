package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Joueur;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JoueurAdapter.class})
@DisplayName("JoueurAdapter @DataJpaTest")
class JoueurAdapterJpaTest {

    @Autowired
    private JoueurAdapter joueurAdapter;

    @Test
    @DisplayName("save + findById doit persister et remapper")
    void saveAndFindById() {
        Joueur joueur = new Joueur(List.of(), LocalDate.of(2000, 1, 1), null);
        joueur.setPseudo("player456");
        joueur.setEmail("player456@mail.com");
        joueur.setMotDePasse("secret");

        Joueur saved = joueurAdapter.save(joueur);

        assertThat(saved.getId()).isNotNull();

        var found = joueurAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPseudo()).isEqualTo("player456");
        assertThat(found.get().getEmail()).isEqualTo("player456@mail.com");
    }

    @Test
    @DisplayName("findByEmail + existsByEmail doivent fonctionner")
    void findByEmailAndExistsByEmail() {
        Joueur joueur = new Joueur(List.of(), LocalDate.of(1999, 12, 31), null);
        joueur.setPseudo("mailtest");
        joueur.setEmail("mailtest@mail.com");
        joueur.setMotDePasse("pwd");
        joueurAdapter.save(joueur);

        assertThat(joueurAdapter.existsByEmail("mailtest@mail.com")).isTrue();
        assertThat(joueurAdapter.existsByEmail("unknown@mail.com")).isFalse();

        var found = joueurAdapter.findByEmail("mailtest@mail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getPseudo()).isEqualTo("mailtest");
    }
}

