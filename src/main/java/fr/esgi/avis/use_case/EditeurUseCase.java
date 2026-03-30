package fr.esgi.avis.use_case;

import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;

import java.util.List;

public interface EditeurUseCase {

    EditeurDtoOut creerUnEditeur(EditeurDtoIn dto);

    EditeurDtoOut mettreAJoueur(Long id, EditeurDtoIn dto);

    EditeurDtoOut recupererUnEditeurParId(Long id);

    List<EditeurDtoOut> recupererTousLesEditeurs();

    void supprimerUnEditeur(Long id);
}