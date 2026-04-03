package fr.esgi.avis.service;

import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import fr.esgi.avis.mapper.AvatarMapper;
import fr.esgi.avis.port.in.AvatarUseCase;
import fr.esgi.avis.port.out.AvatarPort;
import fr.esgi.avis.port.out.JoueurPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvatarService implements AvatarUseCase {

    private final AvatarPort avatarPort;
    private final JoueurPort joueurPort;
    private final AvatarMapper avatarMapper;

    public AvatarService(AvatarPort avatarPort, JoueurPort joueurPort, AvatarMapper avatarMapper) {
        this.avatarPort = avatarPort;
        this.joueurPort = joueurPort;
        this.avatarMapper = avatarMapper;
    }

    @Override
    public AvatarDtoOut creerUnAvatar(AvatarDtoIn dto) {
        Joueur joueur = joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + dto.joueurId()));
        return avatarMapper.toDto(avatarPort.save(avatarMapper.toDomain(dto, joueur)));
    }

    @Override
    public AvatarDtoOut mettreAJour(Long id, AvatarDtoIn dto) {
        avatarPort.findById(id).orElseThrow(() -> new RuntimeException("Avatar non trouvé : " + id));
        Joueur joueur = joueurPort.findById(dto.joueurId())
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé : " + dto.joueurId()));
        return avatarMapper.toDto(avatarPort.save(avatarMapper.toDomain(dto, joueur)));
    }

    @Override
    @Transactional(readOnly = true)
    public AvatarDtoOut recupererUnAvatarParId(Long id) {
        return avatarPort.findById(id)
                .map(avatarMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Avatar non trouvé : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvatarDtoOut> recupererTousLesAvatars() {
        return avatarPort.findAll().stream().map(avatarMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvatarDtoOut> recupererAvatarsParJoueur(Long joueurId) {
        return avatarPort.findAllByJoueurId(joueurId).stream().map(avatarMapper::toDto).toList();
    }

    @Override
    public void supprimerUnAvatar(Long id) {
        avatarPort.deleteById(id);
    }
}
