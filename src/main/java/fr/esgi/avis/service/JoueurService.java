package fr.esgi.avis.service;

import fr.esgi.avis.business.Jeu;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import fr.esgi.avis.mapper.AvisMapper;
import fr.esgi.avis.mapper.JoueurMapper;
import fr.esgi.avis.port.in.JoueurUseCase;
import fr.esgi.avis.port.out.AvisPort;
import fr.esgi.avis.port.out.JeuPort;
import fr.esgi.avis.port.out.JoueurPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JoueurService implements JoueurUseCase {

    private final JoueurPort joueurPort;
    private final AvisPort avisPort;
    private final JeuPort jeuPort;
    private final JoueurMapper joueurMapper;
    private final AvisMapper avisMapper;

    public JoueurService(JoueurPort joueurPort, AvisPort avisPort, JeuPort jeuPort,
                         JoueurMapper joueurMapper, AvisMapper avisMapper) {
        this.joueurPort = joueurPort;
        this.avisPort = avisPort;
        this.jeuPort = jeuPort;
        this.joueurMapper = joueurMapper;
        this.avisMapper = avisMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public JoueurDtoOut seConnecter(String email, String motDePasse) {
        Joueur joueur = joueurPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé pour l'email : " + email));
        if (!joueur.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return joueurMapper.toDto(joueur);
    }

    @Override
    public JoueurDtoOut sInscrire(JoueurDtoIn dto) {
        if (joueurPort.existsByEmail(dto.email())) {
            throw new RuntimeException("Email déjà utilisé : " + dto.email());
        }
        return joueurMapper.toDto(joueurPort.save(joueurMapper.toDomain(dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public JoueurDtoOut trouverParId(Long id) {
        return joueurPort.findById(id)
                .map(joueurMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisDtoOut> listerAvisDuJoueur(Long joueurId) {
        return avisPort.findAllByJoueurId(joueurId).stream().map(avisMapper::toDto).toList();
    }

    @Override
    public AvisDtoOut redigerUnAvis(Long joueurId, AvisDtoIn dto) {
        Joueur joueur = joueurPort.findById(joueurId)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + joueurId));
        Jeu jeu = jeuPort.findById(dto.jeuId())
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé : " + dto.jeuId()));
        return avisMapper.toDto(avisPort.save(avisMapper.toDomain(dto, jeu, joueur, null)));
    }
}
