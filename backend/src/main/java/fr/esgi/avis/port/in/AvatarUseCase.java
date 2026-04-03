package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;

import java.util.List;

public interface AvatarUseCase {

    AvatarDtoOut creerUnAvatar(AvatarDtoIn dto);

    AvatarDtoOut mettreAJour(Long id, AvatarDtoIn dto);

    AvatarDtoOut recupererUnAvatarParId(Long id);

    List<AvatarDtoOut> recupererTousLesAvatars();

    List<AvatarDtoOut> recupererAvatarsParJoueur(Long joueurId);

    void supprimerUnAvatar(Long id);
}
