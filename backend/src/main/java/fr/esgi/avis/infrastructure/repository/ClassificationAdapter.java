package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Classification;
import fr.esgi.avis.infrastructure.entity.ClassificationEntity;
import fr.esgi.avis.port.out.ClassificationPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClassificationAdapter implements ClassificationPort {

    private final ClassificationJpaRepository jpaRepository;

    public ClassificationAdapter(ClassificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Classification save(Classification classification) {
        return toDomain(jpaRepository.save(toEntity(classification)));
    }

    @Override
    public Optional<Classification> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Classification> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private ClassificationEntity toEntity(Classification classification) {
        ClassificationEntity entity = new ClassificationEntity();
        entity.setId(classification.getId());
        entity.setNom(classification.getNom());
        entity.setCouleurRGB(classification.getCouleurRGB());
        return entity;
    }

    private Classification toDomain(ClassificationEntity entity) {
        return new Classification(entity.getId(), entity.getNom(), Collections.emptyList(), entity.getCouleurRGB());
    }
}
