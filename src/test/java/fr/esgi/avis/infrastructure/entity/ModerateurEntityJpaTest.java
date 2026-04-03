package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.ModerateurJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("ModerateurEntity contraintes @DataJpaTest")
class ModerateurEntityJpaTest {

    @Autowired
    private ModerateurJpaRepository moderateurJpaRepository;

    @Test
    @DisplayName("email doit être unique")
    void email_uniqueConstraint() {
        ModerateurEntity m1 = new ModerateurEntity();
        m1.setPseudo("mod1");
        m1.setEmail("unique_mod@mail.com");
        m1.setMotDePasse("pwd");
        m1.setNumeroDeTelephone("0600000000");
        moderateurJpaRepository.saveAndFlush(m1);

        ModerateurEntity m2 = new ModerateurEntity();
        m2.setPseudo("mod2");
        m2.setEmail("unique_mod@mail.com");
        m2.setMotDePasse("pwd");
        m2.setNumeroDeTelephone("0600000001");

        assertThatThrownBy(() -> moderateurJpaRepository.saveAndFlush(m2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("pseudo doit être unique")
    void pseudo_uniqueConstraint() {
        ModerateurEntity m1 = new ModerateurEntity();
        m1.setPseudo("uniquePseudo");
        m1.setEmail("m1@mail.com");
        m1.setMotDePasse("pwd");
        moderateurJpaRepository.saveAndFlush(m1);

        ModerateurEntity m2 = new ModerateurEntity();
        m2.setPseudo("uniquePseudo");
        m2.setEmail("m2@mail.com");
        m2.setMotDePasse("pwd");

        assertThatThrownBy(() -> moderateurJpaRepository.saveAndFlush(m2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("findByEmail doit retourner un modérateur existant")
    void findByEmail_shouldReturnExistingModerateur() {
        ModerateurEntity m = new ModerateurEntity();
        m.setPseudo("findMod");
        m.setEmail("findmod@mail.com");
        m.setMotDePasse("pwd");
        moderateurJpaRepository.saveAndFlush(m);

        assertThat(moderateurJpaRepository.findByEmail("findmod@mail.com")).isPresent();
    }
}

