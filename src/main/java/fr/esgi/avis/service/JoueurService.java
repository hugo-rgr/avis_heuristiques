package fr.esgi.avis.service;

import fr.esgi.avis.business.Avis;
import fr.esgi.avis.business.Jeu;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.use_case.JoueurUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class JoueurService implements JoueurUseCase {

    private final JoueurPort joueurPort;
    private final AvisPort avisPort;
    private final JeuPort jeuPort;

    public JoueurService(JoueurPort joueurPort, AvisPort avisPort, JeuPort jeuPort) {
        this.joueurPort = joueurPort;
        this.avisPort = avisPort;
        this.jeuPort = jeuPort;
    }

    @Override
    @Transactional(readOnly = true)
    public JoueurDtoOut seConnecter(String email, String motDePasse) {
        Joueur joueur = joueurPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé pour l'email : " + email));
        if (!joueur.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return toDto(joueur);
    }

    @Override
    public JoueurDtoOut sInscrire(JoueurDtoIn dto) {
        if (joueurPort.existsByEmail(dto.email())) {
            throw new RuntimeException("Email déjà utilisé : " + dto.email());
        }
        Joueur joueur = new Joueur(Collections.emptyList(), dto.dateDeNaissance(), null);
        joueur.setPseudo(dto.pseudo());
        joueur.setEmail(dto.email());
        joueur.setMotDePasse(dto.motDePasse());
        return toDto(joueurPort.save(joueur));
    }

    @Override
    @Transactional(readOnly = true)
    public JoueurDtoOut trouverParId(Long id) {
        return joueurPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> listerAvisDuJoueur(Long joueurId) {
        return avisPort.findAllByJoueurId(joueurId).stream().map(this::toAvisDto).toList();
    }

    @Override
    public AvisDtoOut redigerUnAvis(Long joueurId, AvisDtoIn dto) {
        Joueur joueur = joueurPort.findById(joueurId)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + joueurId));
        Jeu jeu = jeuPort.findById(dto.jeuId())
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + dto.jeuId()));
        Avis avis = new Avis(null, dto.description(), jeu, joueur, dto.note(), null, dto.dateDEnvoi());
        return toAvisDto(avisPort.save(avis));
    }

    private JoueurDtoOut toDto(Joueur j) {
        String avatarNom = j.getAvatar() != null ? j.getAvatar().getNom() : null;
        return new JoueurDtoOut(j.getId(), j.getPseudo(), j.getEmail(), j.getDateDeNaissance(), avatarNom);
    }

    private AvisDtoOut toAvisDto(Avis a) {
        String jeuNom = a.getJeu() != null ? a.getJeu().getNom() : null;
        String joueurPseudo = a.getJoueur() != null ? a.getJoueur().getPseudo() : null;
        String moderateurPseudo = a.getModerateur() != null ? a.getModerateur().getPseudo() : null;
        return new AvisDtoOut(a.getId(), a.getDescription(), a.getNote(), jeuNom, joueurPseudo, moderateurPseudo, a.getDateDEnvoi());
    }
}
