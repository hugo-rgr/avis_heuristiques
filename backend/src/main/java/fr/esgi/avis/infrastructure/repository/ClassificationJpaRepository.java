package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.ClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationJpaRepository extends JpaRepository<ClassificationEntity, Long> {
}
