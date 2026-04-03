package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;

import java.util.List;

public interface GenreUseCase {

    GenreDtoOut creerUnGenre(GenreDtoIn dto);

    GenreDtoOut mettreAJourUnGenre(Long id, GenreDtoIn dto);

    GenreDtoOut recupererUnGenreParId(Long id);

    List<GenreDtoOut> recupererTouslesGenre();

    void supprimerUnGenre(Long id);
}
