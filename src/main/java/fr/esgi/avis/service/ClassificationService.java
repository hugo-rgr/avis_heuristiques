package fr.esgi.avis.service;

import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.business.Classification;
import fr.esgi.avis.port.out.ClassificationPort;
import fr.esgi.avis.use_case.ClassificationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ClassificationService implements ClassificationUseCase {

    private final ClassificationPort classificationPort;

    public ClassificationService(ClassificationPort classificationPort) {
        this.classificationPort = classificationPort;
    }

    @Override
    public ClassificationDtoOut creerUneClassification(ClassificationDtoIn dto) {
        Classification classification = new Classification(null, dto.nom(), Collections.emptyList(), dto.couleurRGB());
        return toDto(classificationPort.save(classification));
    }

    @Override
    public ClassificationDtoOut mettreAJourUneClassification(Long id, ClassificationDtoIn dto) {
        Classification existing = classificationPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Classification non trouvée : " + id));
        existing.setNom(dto.nom());
        existing.setCouleurRGB(dto.couleurRGB());
        return toDto(classificationPort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public ClassificationDtoOut recupererUneClassificationParId(Long id) {
        return classificationPort.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Classification non trouvée : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationDtoOut> recupererToutesLesClassifications() {
        return classificationPort.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void supprimerUneClassification(Long id) {
        classificationPort.deleteById(id);
    }

    private ClassificationDtoOut toDto(Classification c) {
        return new ClassificationDtoOut(c.getId(), c.getNom(), c.getCouleurRGB());
    }
}
