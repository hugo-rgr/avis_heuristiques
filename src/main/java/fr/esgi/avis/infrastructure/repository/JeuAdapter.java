package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.*;
import fr.esgi.avis.infrastructure.entity.*;
import fr.esgi.avis.port.out.JeuPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JeuAdapter implements JeuPort {

    private final JeuJpaRepository jpaRepository;
    private final GenreJpaRepository genreJpaRepository;
    private final EditeurJpaRepository editeurJpaRepository;
    private final ClassificationJpaRepository classificationJpaRepository;
    private final PlateformeJpaRepository plateformeJpaRepository;

    public JeuAdapter(JeuJpaRepository jpaRepository,
                      GenreJpaRepository genreJpaRepository,
                      EditeurJpaRepository editeurJpaRepository,
                      ClassificationJpaRepository classificationJpaRepository,
                      PlateformeJpaRepository plateformeJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.genreJpaRepository = genreJpaRepository;
        this.editeurJpaRepository = editeurJpaRepository;
        this.classificationJpaRepository = classificationJpaRepository;
        this.plateformeJpaRepository = plateformeJpaRepository;
    }

    @Override
    public Jeu save(Jeu jeu) {
        return toDomain(jpaRepository.save(toEntity(jeu)));
    }

    @Override
    public Optional<Jeu> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Jeu> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Jeu> findAllByGenreId(Long genreId) {
        return jpaRepository.findAllByGenreId(genreId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Jeu> findAllByEditeurId(Long editeurId) {
        return jpaRepository.findAllByEditeurId(editeurId).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private JeuEntity toEntity(Jeu jeu) {
        JeuEntity entity = new JeuEntity();
        entity.setId(jeu.getId());
        entity.setNom(jeu.getNom());
        entity.setDescription(jeu.getDescription());
        entity.setImage(jeu.getImage());
        entity.setPrix(jeu.getPrix());
        entity.setDateDeSortie(jeu.getDateDeSortie());
        if (jeu.getGenre() != null) {
            entity.setGenre(genreJpaRepository.getReferenceById(jeu.getGenre().getId()));
        }
        if (jeu.getEditeur() != null) {
            entity.setEditeur(editeurJpaRepository.getReferenceById(jeu.getEditeur().getId()));
        }
        if (jeu.getClassification() != null) {
            entity.setClassification(classificationJpaRepository.getReferenceById(jeu.getClassification().getId()));
        }
        if (jeu.getPlateformes() != null && !jeu.getPlateformes().isEmpty()) {
            List<Long> ids = jeu.getPlateformes().stream().map(Plateforme::getId).toList();
            entity.setPlateformes(plateformeJpaRepository.findAllByIdIn(ids));
        }
        return entity;
    }

    private Jeu toDomain(JeuEntity entity) {
        Genre genre = entity.getGenre() != null
                ? new Genre(entity.getGenre().getId(), entity.getGenre().getNom(), Collections.emptyList())
                : null;
        Editeur editeur = entity.getEditeur() != null
                ? new Editeur(entity.getEditeur().getId(), entity.getEditeur().getNom(), Collections.emptyList())
                : null;
        Classification classification = entity.getClassification() != null
                ? new Classification(entity.getClassification().getId(), entity.getClassification().getNom(), Collections.emptyList(), entity.getClassification().getCouleurRGB())
                : null;
        List<Plateforme> plateformes = entity.getPlateformes().stream()
                .map(p -> new Plateforme(p.getId(), p.getNom(), Collections.emptyList(), p.getDateDeSortie()))
                .toList();
        return new Jeu(entity.getId(), entity.getNom(), entity.getDescription(), genre,
                entity.getImage(), entity.getPrix(), entity.getDateDeSortie(), editeur, classification, plateformes);
    }
}
