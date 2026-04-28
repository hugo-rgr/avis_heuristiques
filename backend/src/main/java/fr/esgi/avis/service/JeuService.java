package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.exception.ResourceNotFoundException;
import fr.esgi.avis.mapper.JeuMapper;
import fr.esgi.avis.port.in.JeuUseCase;
import fr.esgi.avis.port.out.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AvisPort avisPort;
    private final JeuMapper jeuMapper;

    public JeuService(JeuPort jeuPort, GenrePort genrePort, EditeurPort editeurPort,
                      ClassificationPort classificationPort, PlateformePort plateformePort,
                      AvisPort avisPort, JeuMapper jeuMapper) {
        this.jeuPort = jeuPort;
        this.genrePort = genrePort;
        this.editeurPort = editeurPort;
        this.classificationPort = classificationPort;
        this.plateformePort = plateformePort;
        this.avisPort = avisPort;
        this.jeuMapper = jeuMapper;
    }

    @Override
    public JeuDtoOut creerUnJeu(JeuDtoIn dto) {
        Jeu jeu = jeuPort.save(jeuMapper.toDomain(dto, resolveGenre(dto), resolveEditeur(dto), resolveClassification(dto), resolvePlateformes(dto)));
        return toDtoAvecMoyenne(jeu);
    }

    @Override
    public JeuDtoOut mettreAJourUnJeu(Long id, JeuDtoIn dto) {
        jeuPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jeu", id));
        Jeu jeu = jeuPort.save(jeuMapper.toDomain(id, dto, resolveGenre(dto), resolveEditeur(dto), resolveClassification(dto), resolvePlateformes(dto)));
        return toDtoAvecMoyenne(jeu);
    }

    @Override
    @Transactional(readOnly = true)
    public JeuDtoOut recupererUnJeuParId(Long id) {
        return jeuPort.findById(id)
                .map(this::toDtoAvecMoyenne)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererTousLesJeux() {
        return jeuPort.findAll().stream().map(this::toDtoAvecMoyenne).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JeuDtoOut> recupererTousLesJeux(Pageable pageable) {
        return jeuPort.findAll(pageable).map(this::toDtoAvecMoyenne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnGenre(Long genreId) {
        return jeuPort.findAllByGenreId(genreId).stream().map(this::toDtoAvecMoyenne).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JeuDtoOut> recupererDesJeuxDUnEditeur(Long editeurId) {
        return jeuPort.findAllByEditeurId(editeurId).stream().map(this::toDtoAvecMoyenne).toList();
    }

    @Override
    public void supprimerUnJeu(Long id) {
        jeuPort.deleteById(id);
    }

    // --- Helpers ---

    private JeuDtoOut toDtoAvecMoyenne(Jeu jeu) {
        Double moyenne = avisPort.findNoteMoyenneByJeuId(jeu.getId()).orElse(null);
        jeu.setNoteMoyenne(moyenne);
        return jeuMapper.toDto(jeu);
    }

    private Genre resolveGenre(JeuDtoIn dto) {
        return dto.genreId() != null
                ? genrePort.findById(dto.genreId()).orElseThrow(() -> new ResourceNotFoundException("Genre", dto.genreId()))
                : null;
    }

    private Editeur resolveEditeur(JeuDtoIn dto) {
        return dto.editeurId() != null
                ? editeurPort.findById(dto.editeurId()).orElseThrow(() -> new ResourceNotFoundException("Editeur", dto.editeurId()))
                : null;
    }

    private Classification resolveClassification(JeuDtoIn dto) {
        return dto.classificationId() != null
                ? classificationPort.findById(dto.classificationId()).orElseThrow(() -> new ResourceNotFoundException("Classification", dto.classificationId()))
                : null;
    }

    private List<Plateforme> resolvePlateformes(JeuDtoIn dto) {
        return dto.plateformeIds() != null && !dto.plateformeIds().isEmpty()
                ? plateformePort.findAllByIds(dto.plateformeIds())
                : Collections.emptyList();
    }
}
