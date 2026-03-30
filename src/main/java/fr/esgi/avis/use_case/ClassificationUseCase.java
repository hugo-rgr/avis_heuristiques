package fr.esgi.avis.use_case;

import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;

import java.util.List;

public interface ClassificationUseCase {

    ClassificationDtoOut creerUneClassification(ClassificationDtoIn dto);

    ClassificationDtoOut mettreAJourUneClassification(Long id, ClassificationDtoIn dto);

    ClassificationDtoOut recupererUneClassificationParId(Long id);

    List<ClassificationDtoOut> recupererToutesLesClassifications();
    void supprimerUneClassification(Long id);
}