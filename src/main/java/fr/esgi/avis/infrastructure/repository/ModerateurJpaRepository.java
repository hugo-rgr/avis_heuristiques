package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.ModerateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModerateurJpaRepository extends JpaRepository<ModerateurEntity, Long> {
    Optional<ModerateurEntity> findByEmail(String email);
}
