package fr.esgi.avis.service;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import fr.esgi.avis.port.out.*;
import fr.esgi.avis.port.in.ModerateurUseCase;
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

    public ModerateurService(ModerateurPort moderateurPort, JeuPort jeuPort, AvisPort avisPort,
                             GenrePort genrePort, EditeurPort editeurPort,
                             ClassificationPort classificationPort, PlateformePort plateformePort) {
        this.moderateurPort = moderateurPort;
        this.jeuPort = jeuPort;
        this.avisPort = avisPort;
        this.genrePort = genrePort;
        this.editeurPort = editeurPort;
        this.classificationPort = classificationPort;
        this.plateformePort = plateformePort;
    }

    @Override
    @Transactional(readOnly = true)
    public ModerateurDtoOut seConnecter(String email, String motDePasse) {
        Moderateur moderateur = moderateurPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé pour l'email : " + email));
        if (!moderateur.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return toDto(moderateur);
    }

    @Override
    @Transactional(readOnly = true)
    public ModerateurDtoOut trouverParId(Long id) {
        return moderateurPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + id));
    }

    @Override
    public JeuDtoOut ajouterJeu(Long moderateurId, JeuDtoIn dto) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + moderateurId));
        Jeu jeu = jeuPort.save(fromDto(dto));
        return toJeuDto(jeu);
    }

    @Override
    public void supprimerAvis(Long moderateurId, Long avisId) {
        moderateurPort.findById(moderateurId)
                .orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + moderateurId));
        avisPort.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé : " + avisId));
        avisPort.deleteById(avisId);
    }

    private Jeu fromDto(JeuDtoIn dto) {
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
        return new Jeu(null, dto.nom(), dto.description(), genre, dto.image(), dto.prix(), dto.dateDeSortie(), editeur, classification, plateformes);
    }

    private ModerateurDtoOut toDto(Moderateur m) {
        return new ModerateurDtoOut(m.getId(), m.getPseudo(), m.getEmail(), m.getNumeroDeTelephone());
    }

    private JeuDtoOut toJeuDto(Jeu j) {
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
