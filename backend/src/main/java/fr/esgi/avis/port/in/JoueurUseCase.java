package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import fr.esgi.avis.dto.out.JoueurDtoOut;

import java.util.List;

/**
 * Port d'entrée - cas d'utilisation liés à l'authentification d'un Joueur.
 */
public interface JoueurUseCase {
    JoueurDtoOut sInscrire(JoueurDtoIn joueurDtoIn);
    JoueurDtoOut trouverParId(Long id);

    List<AvisDtoOut> listerAvisDuJoueur(Long joueurId);
    AvisDtoOut redigerUnAvis(Long joueurId, AvisDtoIn avisDtoIn);
}
