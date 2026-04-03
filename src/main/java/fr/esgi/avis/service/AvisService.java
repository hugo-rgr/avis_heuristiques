package fr.esgi.avis.service;

import fr.esgi.avis.business.Avis;
import fr.esgi.avis.business.Jeu;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.out.ModerateurPort;
import fr.esgi.avis.port.in.AvisUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvisService implements AvisUseCase {

    private final AvisPort avisPort;
    private final JeuPort jeuPort;
    private final JoueurPort joueurPort;
    private final ModerateurPort moderateurPort;

    public AvisService(AvisPort avisPort, JeuPort jeuPort, JoueurPort joueurPort, ModerateurPort moderateurPort) {
        this.avisPort = avisPort;
        this.jeuPort = jeuPort;
        this.joueurPort = joueurPort;
        this.moderateurPort = moderateurPort;
    }

    @Override
    public AvisDtoOut creerUnAvis(AvisDtoIn dto) {
        return toDto(avisPort.save(fromDto(null, dto)));
    }

    @Override
    public AvisDtoOut mettreAJourUnAvis(Long id, AvisDtoIn dto) {
        avisPort.findById(id).orElseThrow(() -> new RuntimeException("Avis non trouvé : " + id));
        return toDto(avisPort.save(fromDto(id, dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public AvisDtoOut recupererUnAvisParId(Long id) {
        return avisPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvis() {
        return avisPort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId) {
        return avisPort.findAllByJeuId(jeuId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId) {
        return avisPort.findAllByJoueurId(joueurId).stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUnAvis(Long id) {
        avisPort.deleteById(id);
    }

    private Avis fromDto(Long id, AvisDtoIn dto) {
        Jeu jeu = jeuPort.findById(dto.jeuId())
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + dto.jeuId()));
        Joueur joueur = joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + dto.joueurId()));
        Moderateur moderateur = dto.moderateurId() != null
                ? moderateurPort.findById(dto.moderateurId()).orElseThrow(() -> new RuntimeException("Modérateur non trouvé : " + dto.moderateurId()))
                : null;
        return new Avis(id, dto.description(), jeu, joueur, dto.note(), moderateur, dto.dateDEnvoi());
    }

    private AvisDtoOut toDto(Avis a) {
        String jeuNom = a.getJeu() != null ? a.getJeu().getNom() : null;
        String joueurPseudo = a.getJoueur() != null ? a.getJoueur().getPseudo() : null;
        String moderateurPseudo = a.getModerateur() != null ? a.getModerateur().getPseudo() : null;
        return new AvisDtoOut(a.getId(), a.getDescription(), a.getNote(), jeuNom, joueurPseudo, moderateurPseudo, a.getDateDEnvoi());
    }
}
