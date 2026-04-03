package fr.esgi.avis.service;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import fr.esgi.avis.port.out.AvatarPort;
import fr.esgi.avis.port.out.JoueurPort;
import fr.esgi.avis.port.in.AvatarUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvatarService implements AvatarUseCase {

    private final AvatarPort avatarPort;
    private final JoueurPort joueurPort;

    public AvatarService(AvatarPort avatarPort, JoueurPort joueurPort) {
        this.avatarPort = avatarPort;
        this.joueurPort = joueurPort;
    }

    @Override
    public AvatarDtoOut creerUnAvatar(AvatarDtoIn dto) {
        Joueur joueur = joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + dto.joueurId()));
        Avatar avatar = new Avatar(null, dto.nom(), joueur);
        Avatar saved = avatarPort.save(avatar);
        return toDto(saved, joueur);
    }

    @Override
    public AvatarDtoOut mettreAJour(Long id, AvatarDtoIn dto) {
        Avatar existing = avatarPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Avatar non trouvé : " + id));
        Joueur joueur = joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + dto.joueurId()));
        existing.setNom(dto.nom());
        existing.setJoueur(joueur);
        Avatar saved = avatarPort.save(existing);
        return toDto(saved, joueur);
    }

    @Override
    @Transactional(readOnly = true)
    public AvatarDtoOut recupererUnAvatarParId(Long id) {
        Avatar avatar = avatarPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Avatar non trouvé : " + id));
        return toDtoFromAvatar(avatar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvatarDtoOut> recupererTousLesAvatars() {
        return avatarPort.findAll().stream().map(this::toDtoFromAvatar).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvatarDtoOut> recupererAvatarsParJoueur(Long joueurId) {
        return avatarPort.findAllByJoueurId(joueurId).stream().map(this::toDtoFromAvatar).toList();
    }

    @Override
    public void supprimerUnAvatar(Long id) {
        avatarPort.deleteById(id);
    }

    private AvatarDtoOut toDto(Avatar avatar, Joueur joueur) {
        return new AvatarDtoOut(avatar.getId(), avatar.getNom(), joueur.getId(), joueur.getPseudo());
    }

    private AvatarDtoOut toDtoFromAvatar(Avatar avatar) {
        Long joueurId = avatar.getJoueur() != null ? avatar.getJoueur().getId() : null;
        String joueurPseudo = avatar.getJoueur() != null ? avatar.getJoueur().getPseudo() : null;
        return new AvatarDtoOut(avatar.getId(), avatar.getNom(), joueurId, joueurPseudo);
    }
}
