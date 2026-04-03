package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.mapper.JeuMapper;
import fr.esgi.avis.port.in.JeuUseCase;
import fr.esgi.avis.port.out.*;
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
    private final JeuMapper jeuMapper;

    public JeuService(JeuPort jeuPort, GenrePort genrePort, EditeurPort editeurPort,
                      ClassificationPort classificationPort, PlateformePort plateformePort,
                      JeuMapper jeuMapper) {
        this.jeuPort = jeuPort;
        this.genrePort = genrePort;
        this.editeurPort = editeurPort;
        this.classificationPort = classificationPort;
        this.plateformePort = plateformePort;
        this.jeuMapper = jeuMapper;
    }

    @Override
    public JeuDtoOut creerUnJeu(JeuDtoIn dto) {
        return jeuMapper.toDto(jeuPort.save(jeuMapper.toDomain(dto, resolveGenre(dto), resolveEditeur(dto), resolveClassification(dto), resolvePlateformes(dto))));
    }

    @Override
    public JeuDtoOut mettreAJourUnJeu(Long id, JeuDtoIn dto) {
        jeuPort.findById(id).orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + id));
        return jeuMapper.toDto(jeuPort.save(jeuMapper.toDomain(dto, resolveGenre(dto), resolveEditeur(dto), resolveClassification(dto), resolvePlateformes(dto))));
    }

    @Override
    @Transactional(readOnly = true)
    public JeuDtoOut recupererUnJeuParId(Long id) {
        return jeuPort.findById(id)
                .map(jeuMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererTousLesJeux() {
        return jeuPort.findAll().stream().map(jeuMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnGenre(Long genreId) {
        return jeuPort.findAllByGenreId(genreId).stream().map(jeuMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnEditeur(Long editeurId) {
        return jeuPort.findAllByEditeurId(editeurId).stream().map(jeuMapper::toDto).toList();
    }

    @Override
    public void supprimerUnJeu(Long id) {
        jeuPort.deleteById(id);
    }

    private Genre resolveGenre(JeuDtoIn dto) {
        return dto.genreId() != null
                ? genrePort.findById(dto.genreId()).orElseThrow(() -> new RuntimeException("Genre non trouvé : " + dto.genreId()))
                : null;
    }

    private Editeur resolveEditeur(JeuDtoIn dto) {
        return dto.editeurId() != null
                ? editeurPort.findById(dto.editeurId()).orElseThrow(() -> new RuntimeException("Editeur non trouvé : " + dto.editeurId()))
                : null;
    }

    private Classification resolveClassification(JeuDtoIn dto) {
        return dto.classificationId() != null
                ? classificationPort.findById(dto.classificationId()).orElseThrow(() -> new RuntimeException("Classification non trouvée : " + dto.classificationId()))
                : null;
    }

    private List<Plateforme> resolvePlateformes(JeuDtoIn dto) {
        return dto.plateformeIds() != null && !dto.plateformeIds().isEmpty()
                ? plateformePort.findAllByIds(dto.plateformeIds())
                : Collections.emptyList();
    }
}
