package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import fr.esgi.avis.dto.out.ModerateurDtoOut;

public interface ModerateurUseCase {
    ModerateurDtoOut seConnecter(String email, String motDePasse);
    ModerateurDtoOut trouverParId(Long id);

    JeuDtoOut ajouterJeu(Long moderateurId, JeuDtoIn jeuDtoIn);
    void supprimerAvis(Long moderateurId, Long avisId);
}
