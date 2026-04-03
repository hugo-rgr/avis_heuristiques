package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.JoueurJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("JoueurEntity + JoueurJpaRepository @DataJpaTest")
class JoueurEntityJpaTest {

    @Autowired
    private JoueurJpaRepository joueurJpaRepository;

    @Test
    @DisplayName("email doit être unique")
    void email_uniqueConstraint() {
        JoueurEntity j1 = new JoueurEntity();
        j1.setPseudo("u1");
        j1.setEmail("unique@mail.com");
        j1.setMotDePasse("pwd");
        j1.setDateDeNaissance(LocalDate.of(1995, 5, 15));
        joueurJpaRepository.saveAndFlush(j1);

        JoueurEntity j2 = new JoueurEntity();
        j2.setPseudo("u2");
        j2.setEmail("unique@mail.com");
        j2.setMotDePasse("pwd");
        j2.setDateDeNaissance(LocalDate.of(1990, 1, 1));

        // Attention: après ce type d'exception, Hibernate peut laisser la session dans un état instable.
        // On s'arrête donc à l'assertion d'exception dans ce test.
        assertThatThrownBy(() -> joueurJpaRepository.saveAndFlush(j2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("findByEmail doit retourner un joueur existant")
    void findByEmail_shouldReturnExistingJoueur() {
        JoueurEntity j1 = new JoueurEntity();
        j1.setPseudo("u3");
        j1.setEmail("find@mail.com");
        j1.setMotDePasse("pwd");
        j1.setDateDeNaissance(LocalDate.of(1995, 5, 15));
        joueurJpaRepository.saveAndFlush(j1);

        assertThat(joueurJpaRepository.findByEmail("find@mail.com")).isPresent();
    }
}
