package fr.esgi.avis.service;

import fr.esgi.avis.business.Classification;
import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import fr.esgi.avis.mapper.ClassificationMapper;
import fr.esgi.avis.port.in.ClassificationUseCase;
import fr.esgi.avis.port.out.ClassificationPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassificationService implements ClassificationUseCase {

    private final ClassificationPort classificationPort;
    private final ClassificationMapper classificationMapper;

    public ClassificationService(ClassificationPort classificationPort, ClassificationMapper classificationMapper) {
        this.classificationPort = classificationPort;
        this.classificationMapper = classificationMapper;
    }

    @Override
    public ClassificationDtoOut creerUneClassification(ClassificationDtoIn dto) {
        return classificationMapper.toDto(classificationPort.save(classificationMapper.toDomain(dto)));
    }

    @Override
    public ClassificationDtoOut mettreAJourUneClassification(Long id, ClassificationDtoIn dto) {
        Classification existing = classificationPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Classification non trouvée : " + id));
        existing.setNom(dto.nom());
        existing.setCouleurRGB(dto.couleurRGB());
        return classificationMapper.toDto(classificationPort.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public ClassificationDtoOut recupererUneClassificationParId(Long id) {
        return classificationPort.findById(id)
                .map(classificationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Classification non trouvée : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationDtoOut> recupererToutesLesClassifications() {
        return classificationPort.findAll().stream().map(classificationMapper::toDto).toList();
    }

    @Override
    public void supprimerUneClassification(Long id) {
        classificationPort.deleteById(id);
    }
}
