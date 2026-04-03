package fr.esgi.avis.port.in;

import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;

import java.util.List;

public interface EditeurUseCase {

    EditeurDtoOut creerUnEditeur(EditeurDtoIn dto);

    EditeurDtoOut mettreAJour(Long id, EditeurDtoIn dto);

    EditeurDtoOut recupererUnEditeurParId(Long id);

    List<EditeurDtoOut> recupererTousLesEditeurs();

    void supprimerUnEditeur(Long id);
}
