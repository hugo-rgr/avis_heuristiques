package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.port.out.*;
import fr.esgi.avis.port.in.JeuUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class JeuService implements JeuUseCase {

    private final JeuPort jeuPort;
    private final GenrePort genrePort;
    private final EditeurPort editeurPort;
    private final ClassificationPort classificationPort;
    private final PlateformePort plateformePort;

    public JeuService(JeuPort jeuPort, GenrePort genrePort, EditeurPort editeurPort,
                      ClassificationPort classificationPort, PlateformePort plateformePort) {
        this.jeuPort = jeuPort;
        this.genrePort = genrePort;
        this.editeurPort = editeurPort;
        this.classificationPort = classificationPort;
        this.plateformePort = plateformePort;
    }

    @Override
    public JeuDtoOut creerUnJeu(JeuDtoIn dto) {
        return toDto(jeuPort.save(fromDto(null, dto)));
    }

    @Override
    public JeuDtoOut mettreAJourUnJeu(Long id, JeuDtoIn dto) {
        jeuPort.findById(id).orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + id));
        return toDto(jeuPort.save(fromDto(id, dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public JeuDtoOut recupererUnJeuParId(Long id) {
        return jeuPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererTousLesJeux() {
        return jeuPort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnGenre(Long genreId) {
        return jeuPort.findAllByGenreId(genreId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnEditeur(Long editeurId) {
        return jeuPort.findAllByEditeurId(editeurId).stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUnJeu(Long id) {
        jeuPort.deleteById(id);
    }

    private Jeu fromDto(Long id, JeuDtoIn dto) {
        Genre genre = dto.genreId() != null
                ? genrePort.findById(dto.genreId()).orElseThrow(() -> new RuntimeException("Genre non trouvé : " + dto.genreId()))
                : null;
        Editeur editeur = dto.editeurId() != null
                ? editeurPort.findById(dto.editeurId()).orElseThrow(() -> new RuntimeException("Editeur non trouvé : " + dto.editeurId()))
                : null;
        Classification classification = dto.classificationId() != null
                ? classificationPort.findById(dto.classificationId()).orElseThrow(() -> new RuntimeException("Classification non trouvée : " + dto.classificationId()))
                : null;
        List<Plateforme> plateformes = dto.plateformeIds() != null && !dto.plateformeIds().isEmpty()
                ? plateformePort.findAllByIds(dto.plateformeIds())
                : Collections.emptyList();
        return new Jeu(id, dto.nom(), dto.description(), genre, dto.image(), dto.prix(), dto.dateDeSortie(), editeur, classification, plateformes);
    }

    private JeuDtoOut toDto(Jeu j) {
        String genreNom = j.getGenre() != null ? j.getGenre().getNom() : null;
        String editeurNom = j.getEditeur() != null ? j.getEditeur().getNom() : null;
        String classificationNom = j.getClassification() != null ? j.getClassification().getNom() : null;
        List<String> plateformes = j.getPlateformes() != null
                ? j.getPlateformes().stream().map(Plateforme::getNom).toList()
                : Collections.emptyList();
        return new JeuDtoOut(j.getId(), j.getNom(), j.getDescription(), j.getImage(), j.getPrix(),
                j.getDateDeSortie(), genreNom, editeurNom, classificationNom, plateformes);
    }
}
