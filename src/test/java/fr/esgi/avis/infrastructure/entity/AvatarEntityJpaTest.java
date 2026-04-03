package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.AvatarJpaRepository;
import fr.esgi.avis.infrastructure.repository.JoueurJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("AvatarEntity contraintes @DataJpaTest")
class AvatarEntityJpaTest {

    @Autowired
    private AvatarJpaRepository avatarJpaRepository;

    @Autowired
    private JoueurJpaRepository joueurJpaRepository;

    @Test
    @DisplayName("joueur est obligatoire (nullable=false)")
    void joueur_isRequired() {
        AvatarEntity avatar = new AvatarEntity();
        avatar.setNom("NoPlayer");

        assertThatThrownBy(() -> avatarJpaRepository.saveAndFlush(avatar))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("un joueur ne peut avoir qu'un avatar (unique joueur_id)")
    void joueurId_isUnique() {
        JoueurEntity joueur = new JoueurEntity();
        joueur.setPseudo("p1");
        joueur.setEmail("p1@mail.com");
        joueur.setMotDePasse("pwd");
        joueur.setDateDeNaissance(LocalDate.of(1995, 5, 15));
        joueur = joueurJpaRepository.saveAndFlush(joueur);

        AvatarEntity a1 = new AvatarEntity();
        a1.setNom("A1");
        a1.setJoueur(joueur);
        avatarJpaRepository.saveAndFlush(a1);

        AvatarEntity a2 = new AvatarEntity();
        a2.setNom("A2");
        a2.setJoueur(joueur);

        assertThatThrownBy(() -> avatarJpaRepository.saveAndFlush(a2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

