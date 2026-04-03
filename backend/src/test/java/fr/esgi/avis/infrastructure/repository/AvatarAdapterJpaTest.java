package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.infrastructure.entity.JoueurEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AvatarAdapter.class})
@DisplayName("AvatarAdapter @DataJpaTest")
class AvatarAdapterJpaTest {

    @Autowired
    private AvatarAdapter avatarAdapter;

    @Autowired
    private JoueurJpaRepository joueurJpaRepository;

    @Autowired
    private AvatarJpaRepository avatarJpaRepository;

    @Test
    @DisplayName("save + findById doit persister et remapper l'avatar et son joueur")
    void saveAndFindById() {
        JoueurEntity joueurEntity = joueurJpaRepository.save(TestEntities.joueurEntity("gamer123", "gamer123@mail.com"));

        Avatar avatar = new Avatar(null, "Knight Avatar", toDomainStub(joueurEntity.getId()));
        Avatar saved = avatarAdapter.save(avatar);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Knight Avatar");
        assertThat(saved.getJoueur()).isNotNull();
        assertThat(saved.getJoueur().getId()).isEqualTo(joueurEntity.getId());

        var found = avatarAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Knight Avatar");
        assertThat(found.get().getJoueur()).isNotNull();
        assertThat(found.get().getJoueur().getPseudo()).isEqualTo("gamer123");
    }

    @Test
    @DisplayName("findAllByJoueurId doit filtrer par joueur")
    void findAllByJoueurId() {
        JoueurEntity j1 = joueurJpaRepository.save(TestEntities.joueurEntity("p1", "p1@mail.com"));
        JoueurEntity j2 = joueurJpaRepository.save(TestEntities.joueurEntity("p2", "p2@mail.com"));

        avatarAdapter.save(new Avatar(null, "A1", toDomainStub(j1.getId())));
        avatarAdapter.save(new Avatar(null, "A2", toDomainStub(j2.getId())));

        List<Avatar> avatarsJ1 = avatarAdapter.findAllByJoueurId(j1.getId());
        assertThat(avatarsJ1).hasSize(1);
        assertThat(avatarsJ1.getFirst().getNom()).isEqualTo("A1");
    }

    @Test
    @DisplayName("deleteById doit supprimer l'avatar")
    void deleteById() {
        JoueurEntity joueurEntity = joueurJpaRepository.save(TestEntities.joueurEntity("del", "del@mail.com"));
        Avatar saved = avatarAdapter.save(new Avatar(null, "ToDelete", toDomainStub(joueurEntity.getId())));

        avatarAdapter.deleteById(saved.getId());

        assertThat(avatarJpaRepository.findById(saved.getId())).isEmpty();
    }

    private static Joueur toDomainStub(Long joueurId) {
        Joueur joueur = new Joueur(List.of(), LocalDate.of(1995, 5, 15), null);
        joueur.setId(joueurId);
        return joueur;
    }
}
