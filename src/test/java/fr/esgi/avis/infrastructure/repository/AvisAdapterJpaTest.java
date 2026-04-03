package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.*;
import fr.esgi.avis.infrastructure.entity.JeuEntity;
import fr.esgi.avis.infrastructure.entity.JoueurEntity;
import fr.esgi.avis.infrastructure.entity.ModerateurEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AvisAdapter.class)
@DisplayName("AvisAdapter @DataJpaTest")
class AvisAdapterJpaTest {

    @Autowired
    private AvisAdapter avisAdapter;

    @Autowired
    private JeuJpaRepository jeuJpaRepository;

    @Autowired
    private JoueurJpaRepository joueurJpaRepository;

    @Autowired
    private ModerateurJpaRepository moderateurJpaRepository;

    @Test
    @DisplayName("save + findById remappe jeu/joueur/moderateur")
    void saveAndFindById_withRelations() {
        JeuEntity jeu = jeuJpaRepository.save(entityJeu("Game1"));
        JoueurEntity joueur = joueurJpaRepository.save(TestEntities.joueurEntity("p1", "p1@mail.com"));
        ModerateurEntity mod = moderateurJpaRepository.save(entityModerateur());

        Avis avis = new Avis(null,
                "Super jeu",
                new Jeu(jeu.getId(), null, null, null, null, 0, null, null, null, List.of()),
                domainJoueurStub(joueur.getId()),
                4.5f,
                domainModerateurStub(mod.getId()),
                LocalDate.of(2025, 1, 1)
        );

        Avis saved = avisAdapter.save(avis);
        assertThat(saved.getId()).isNotNull();

        var found = avisAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getJeu()).isNotNull();
        assertThat(found.get().getJeu().getId()).isEqualTo(jeu.getId());
        assertThat(found.get().getJoueur().getId()).isEqualTo(joueur.getId());
        assertThat(found.get().getModerateur().getId()).isEqualTo(mod.getId());
    }

    @Test
    @DisplayName("findAllByJeuId et findAllByJoueurId doivent filtrer")
    void findAllByJeuId_and_findAllByJoueurId() {
        JeuEntity jeu1 = jeuJpaRepository.save(entityJeu("J1"));
        JeuEntity jeu2 = jeuJpaRepository.save(entityJeu("J2"));

        JoueurEntity j1 = joueurJpaRepository.save(TestEntities.joueurEntity("u1", "u1@mail.com"));
        JoueurEntity j2 = joueurJpaRepository.save(TestEntities.joueurEntity("u2", "u2@mail.com"));

        avisAdapter.save(new Avis(null, "a1", new Jeu(jeu1.getId(), null, null, null, null, 0, null, null, null, List.of()), domainJoueurStub(j1.getId()), 3f, null, LocalDate.of(2025, 1, 1)));
        avisAdapter.save(new Avis(null, "a2", new Jeu(jeu2.getId(), null, null, null, null, 0, null, null, null, List.of()), domainJoueurStub(j1.getId()), 4f, null, LocalDate.of(2025, 1, 2)));
        avisAdapter.save(new Avis(null, "a3", new Jeu(jeu1.getId(), null, null, null, null, 0, null, null, null, List.of()), domainJoueurStub(j2.getId()), 5f, null, LocalDate.of(2025, 1, 3)));

        assertThat(avisAdapter.findAllByJeuId(jeu1.getId())).extracting(Avis::getDescription)
                .containsExactlyInAnyOrder("a1", "a3");

        assertThat(avisAdapter.findAllByJoueurId(j1.getId())).extracting(Avis::getDescription)
                .containsExactlyInAnyOrder("a1", "a2");
    }

    @Test
    @DisplayName("deleteById doit supprimer")
    void deleteById() {
        JeuEntity jeu = jeuJpaRepository.save(entityJeu("DelGame"));
        JoueurEntity joueur = joueurJpaRepository.save(TestEntities.joueurEntity("del", "del@mail.com"));

        Avis saved = avisAdapter.save(new Avis(null, "todel", new Jeu(jeu.getId(), null, null, null, null, 0, null, null, null, List.of()), domainJoueurStub(joueur.getId()), 1f, null, LocalDate.of(2025, 1, 1)));

        avisAdapter.deleteById(saved.getId());
        assertThat(avisAdapter.findById(saved.getId())).isEmpty();
    }

    private static JeuEntity entityJeu(String nom) {
        JeuEntity e = new JeuEntity();
        e.setNom(nom);
        e.setPrix(10f);
        return e;
    }

    private static ModerateurEntity entityModerateur() {
        ModerateurEntity e = new ModerateurEntity();
        e.setPseudo("mod");
        e.setEmail("mod@mail.com");
        e.setMotDePasse("pwd");
        e.setNumeroDeTelephone("0600000000");
        return e;
    }

    private static Joueur domainJoueurStub(Long id) {
        Joueur j = new Joueur(List.of(), LocalDate.of(1990, 1, 1), null);
        j.setId(id);
        return j;
    }

    private static Moderateur domainModerateurStub(Long id) {
        Moderateur m = new Moderateur("0600000000");
        m.setId(id);
        return m;
    }
}
