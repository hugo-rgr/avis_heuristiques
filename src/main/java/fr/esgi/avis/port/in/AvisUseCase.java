package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;

import java.util.List;

public interface AvisUseCase {

    AvisDtoOut creerUnAvis(AvisDtoIn avisDtoIn);

    AvisDtoOut mettreAJourUnAvis(Long id, AvisDtoIn avisDtoIn);

    AvisDtoOut recupererUnAvisParId(Long id);

    List<AvisDtoOut> recupererTousLesAvis();

    List<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId);

    List<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId);

    void supprimerUnAvis(Long id);
}
