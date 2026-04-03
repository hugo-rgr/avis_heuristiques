package fr.esgi.avis.infrastructure.entity;

import fr.esgi.avis.infrastructure.repository.AvisJpaRepository;
import fr.esgi.avis.infrastructure.repository.JeuJpaRepository;
import fr.esgi.avis.infrastructure.repository.JoueurJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("AvisEntity contraintes @DataJpaTest")
class AvisEntityJpaTest {

    @Autowired
    private AvisJpaRepository avisJpaRepository;

    @Autowired
    private JeuJpaRepository jeuJpaRepository;

    @Autowired
    private JoueurJpaRepository joueurJpaRepository;

    @Test
    @DisplayName("jeu est obligatoire (nullable=false)")
    void jeu_isRequired() {
        JoueurEntity joueur = new JoueurEntity();
        joueur.setPseudo("p1");
        joueur.setEmail("p1@mail.com");
        joueur.setMotDePasse("pwd");
        joueur = joueurJpaRepository.saveAndFlush(joueur);

        AvisEntity avis = new AvisEntity();
        avis.setJoueur(joueur);
        avis.setNote(3f);

        assertThatThrownBy(() -> avisJpaRepository.saveAndFlush(avis))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("joueur est obligatoire (nullable=false)")
    void joueur_isRequired() {
        JeuEntity jeu = new JeuEntity();
        jeu.setNom("G");
        jeu.setPrix(10f);
        jeu = jeuJpaRepository.saveAndFlush(jeu);

        AvisEntity avis = new AvisEntity();
        avis.setJeu(jeu);
        avis.setNote(3f);

        assertThatThrownBy(() -> avisJpaRepository.saveAndFlush(avis))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("note a une valeur par défaut (float primitif) si non renseignée")
    void note_defaultsToZeroWhenNotSet() {
        JeuEntity jeu = new JeuEntity();
        jeu.setNom("G2");
        jeu.setPrix(10f);
        jeu = jeuJpaRepository.saveAndFlush(jeu);

        JoueurEntity joueur = new JoueurEntity();
        joueur.setPseudo("p2");
        joueur.setEmail("p2@mail.com");
        joueur.setMotDePasse("pwd");
        joueur = joueurJpaRepository.saveAndFlush(joueur);

        AvisEntity avis = new AvisEntity();
        avis.setJeu(jeu);
        avis.setJoueur(joueur);
        // note non set => 0.0f

        AvisEntity saved = avisJpaRepository.saveAndFlush(avis);

        AvisEntity reloaded = avisJpaRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getNote()).isEqualTo(0.0f);
    }
}
