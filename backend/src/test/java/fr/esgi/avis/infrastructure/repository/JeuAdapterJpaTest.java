package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.*;
import fr.esgi.avis.infrastructure.entity.ClassificationEntity;
import fr.esgi.avis.infrastructure.entity.EditeurEntity;
import fr.esgi.avis.infrastructure.entity.GenreEntity;
import fr.esgi.avis.infrastructure.entity.PlateformeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JeuAdapter.class)
@DisplayName("JeuAdapter @DataJpaTest")
class JeuAdapterJpaTest {

    @Autowired
    private JeuAdapter jeuAdapter;

    @Autowired
    private GenreJpaRepository genreJpaRepository;

    @Autowired
    private EditeurJpaRepository editeurJpaRepository;

    @Autowired
    private ClassificationJpaRepository classificationJpaRepository;

    @Autowired
    private PlateformeJpaRepository plateformeJpaRepository;

    @Test
    @DisplayName("save + findById remappe genre/editeur/classification/plateformes")
    void saveAndFindById_withRelations() {
        GenreEntity genre = genreJpaRepository.save(entityGenre("Action"));
        EditeurEntity editeur = editeurJpaRepository.save(entityEditeur("Ubisoft"));
        ClassificationEntity classification = classificationJpaRepository.save(entityClassification());
        PlateformeEntity p1 = plateformeJpaRepository.save(entityPlateforme("PC", LocalDate.of(1990, 1, 1)));
        PlateformeEntity p2 = plateformeJpaRepository.save(entityPlateforme("PS5", LocalDate.of(2020, 11, 19)));

        Jeu jeu = new Jeu(null,
                "Assassin",
                "desc",
                new Genre(genre.getId(), null, List.of()),
                "img",
                49.99f,
                LocalDate.of(2024, 1, 1),
                new Editeur(editeur.getId(), null, List.of()),
                new Classification(classification.getId(), null, List.of(), null),
                List.of(new Plateforme(p1.getId(), null, List.of(), null), new Plateforme(p2.getId(), null, List.of(), null))
        );

        Jeu saved = jeuAdapter.save(jeu);
        assertThat(saved.getId()).isNotNull();

        var found = jeuAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Assassin");
        assertThat(found.get().getGenre()).isNotNull();
        assertThat(found.get().getGenre().getId()).isEqualTo(genre.getId());
        assertThat(found.get().getEditeur().getId()).isEqualTo(editeur.getId());
        assertThat(found.get().getClassification().getId()).isEqualTo(classification.getId());
        assertThat(found.get().getPlateformes()).extracting(Plateforme::getId)
                .containsExactlyInAnyOrder(p1.getId(), p2.getId());
    }

    @Test
    @DisplayName("findAllByGenreId et findAllByEditeurId doivent filtrer")
    void findAllByGenreId_and_findAllByEditeurId() {
        GenreEntity g1 = genreJpaRepository.save(entityGenre("RPG"));
        GenreEntity g2 = genreJpaRepository.save(entityGenre("FPS"));

        EditeurEntity e1 = editeurJpaRepository.save(entityEditeur("Bethesda"));
        EditeurEntity e2 = editeurJpaRepository.save(entityEditeur("EA"));

        Jeu j1 = jeuAdapter.save(minimalJeu("Skyrim", g1.getId(), e1.getId()));
        Jeu j2 = jeuAdapter.save(minimalJeu("Doom", g2.getId(), e1.getId()));
        Jeu j3 = jeuAdapter.save(minimalJeu("FIFA", g1.getId(), e2.getId()));

        assertThat(jeuAdapter.findAllByGenreId(g1.getId())).extracting(Jeu::getId)
                .contains(j1.getId(), j3.getId())
                .doesNotContain(j2.getId());

        assertThat(jeuAdapter.findAllByEditeurId(e1.getId())).extracting(Jeu::getId)
                .contains(j1.getId(), j2.getId())
                .doesNotContain(j3.getId());
    }

    @Test
    @DisplayName("deleteById doit supprimer")
    void deleteById() {
        GenreEntity g = genreJpaRepository.save(entityGenre("DeleteGenre"));
        EditeurEntity e = editeurJpaRepository.save(entityEditeur("DeleteEditeur"));
        Jeu saved = jeuAdapter.save(minimalJeu("ToDelete", g.getId(), e.getId()));

        jeuAdapter.deleteById(saved.getId());
        assertThat(jeuAdapter.findById(saved.getId())).isEmpty();
    }

    private static GenreEntity entityGenre(String nom) {
        GenreEntity e = new GenreEntity();
        e.setNom(nom);
        return e;
    }

    private static EditeurEntity entityEditeur(String nom) {
        EditeurEntity e = new EditeurEntity();
        e.setNom(nom);
        return e;
    }

    private static ClassificationEntity entityClassification() {
        ClassificationEntity e = new ClassificationEntity();
        e.setNom("PEGI 16");
        e.setCouleurRGB("#FF0000");
        return e;
    }

    private static PlateformeEntity entityPlateforme(String nom, LocalDate date) {
        PlateformeEntity e = new PlateformeEntity();
        e.setNom(nom);
        e.setDateDeSortie(date);
        return e;
    }

    private static Jeu minimalJeu(String nom, Long genreId, Long editeurId) {
        return new Jeu(null,
                nom,
                null,
                genreId != null ? new Genre(genreId, null, List.of()) : null,
                null,
                10f,
                null,
                editeurId != null ? new Editeur(editeurId, null, List.of()) : null,
                null,
                List.of());
    }
}
