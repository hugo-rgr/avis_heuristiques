package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;

import java.util.List;

public interface PlateformeUseCase {

    PlatefomeDtoOut creerUnePlateforme(PlatefomeDtoIn dto);

    PlatefomeDtoOut mettreAJour(Long id, PlatefomeDtoIn dto);

    PlatefomeDtoOut recupererUnePlateformeParId(Long id);

    List<PlatefomeDtoOut> recupererToutesLesPlateformes();

    void supprimerUnePlateforme(Long id);
}
