package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvatarJpaRepository extends JpaRepository<AvatarEntity, Long> {
    List<AvatarEntity> findAllByJoueurId(Long joueurId);
}
