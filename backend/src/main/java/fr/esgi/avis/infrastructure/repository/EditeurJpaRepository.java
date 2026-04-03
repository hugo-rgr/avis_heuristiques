package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.EditeurEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditeurJpaRepository extends JpaRepository<EditeurEntity, Long> {
}
