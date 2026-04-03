package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.AvisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisJpaRepository extends JpaRepository<AvisEntity, Long> {
    List<AvisEntity> findAllByJeuId(Long jeuId);
    List<AvisEntity> findAllByJoueurId(Long joueurId);
}
