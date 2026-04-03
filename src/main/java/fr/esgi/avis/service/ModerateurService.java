package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.mapper.JeuMapper;
import fr.esgi.avis.mapper.ModerateurMapper;
import fr.esgi.avis.port.in.ModerateurUseCase;
import fr.esgi.avis.port.out.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ModerateurService implements ModerateurUseCase {

    private final ModerateurPort moderateurPort;
    private final JeuPort jeuPort;
    private final AvisPort avisPort;
    private final GenrePort genrePort;
    private final EditeurPort editeurPort;
    private final ClassificationPort classificationPort;
    private final PlateformePort plateformePort;
    private final ModerateurMapper moderateurMapper;
    private final JeuMapper jeuMapper;

    public ModerateurService(ModerateurPort moderateurPort, JeuPort jeuPort, AvisPort avisPort,
                             GenrePort genrePort, EditeurPort editeurPort,
                             ClassificationPort classificationPort, PlateformePort plateformePort,
                             ModerateurMapper moderateurMapper, JeuMapper jeuMapper) {
        this.moderateurPort = moderateurPort;
        this.jeuPort = jeuPort;
        this.avisPort = avisPort;
        this.genrePort = genrePort;
        this.editeurPort = editeurPort;
        this.classificationPort = classificationPort;
        this.plateformePort = plateformePort;
        this.moderateurMapper = moderateurMapper;
        this.jeuMapper = jeuMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ModerateurDtoOut seConnecter(String email, String motDePasse) {
        Moderateur moderateur = moderateurPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé pour l'email : " + email));
        if (!moderateur.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return moderateurMapper.toDto(moderateur);
    }

    @Override
    @Transactional(readOnly = true)
    public ModerateurDtoOut trouverParId(Long id) {
        return moderateurPort.findById(id)
                .map(moderateurMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + id));
    }

    @Override
    public JeuDtoOut ajouterJeu(Long moderateurId, JeuDtoIn dto) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + moderateurId));
        Genre genre = dto.genreId() != null ? genrePort.findById(dto.genreId()).orElseThrow(() -> new RuntimeException("Genre non trouvé : " + dto.genreId())) : null;
        Editeur editeur = dto.editeurId() != null ? editeurPort.findById(dto.editeurId()).orElseThrow(() -> new RuntimeException("Editeur non trouvé : " + dto.editeurId())) : null;
        Classification classification = dto.classificationId() != null ? classificationPort.findById(dto.classificationId()).orElseThrow(() -> new RuntimeException("Classification non trouvée : " + dto.classificationId())) : null;
        List<Plateforme> plateformes = dto.plateformeIds() != null && !dto.plateformeIds().isEmpty() ? plateformePort.findAllByIds(dto.plateformeIds()) : Collections.emptyList();
        return jeuMapper.toDto(jeuPort.save(jeuMapper.toDomain(dto, genre, editeur, classification, plateformes)));
    }

    @Override
    public void supprimerAvis(Long moderateurId, Long avisId) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + moderateurId));
        avisPort.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé : " + avisId));
        avisPort.deleteById(avisId);
    }
}
