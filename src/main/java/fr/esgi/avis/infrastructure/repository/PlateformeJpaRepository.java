package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.PlateformeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlateformeJpaRepository extends JpaRepository<PlateformeEntity, Long> {
    List<PlateformeEntity> findAllByIdIn(List<Long> ids);
}
