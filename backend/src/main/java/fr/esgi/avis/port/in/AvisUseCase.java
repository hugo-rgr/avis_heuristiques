package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AvisUseCase {

    AvisDtoOut creerUnAvis(AvisDtoIn avisDtoIn);

    AvisDtoOut mettreAJourUnAvis(Long id, AvisDtoIn avisDtoIn);

    AvisDtoOut recupererUnAvisParId(Long id);

    List<AvisDtoOut> recupererTousLesAvis();

    Page<AvisDtoOut> recupererTousLesAvis(Pageable pageable);

    List<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId);

    Page<AvisDtoOut> recupererTousLesAvisParJeu(Long jeuId, Pageable pageable);

    List<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId);

    Page<AvisDtoOut> recupererTousLesAvisParJoueur(Long joueurId, Pageable pageable);

    void supprimerUnAvis(Long id);
}
