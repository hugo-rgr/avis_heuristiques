package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.JoueurEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoueurJpaRepository extends JpaRepository<JoueurEntity, Long> {
    Optional<JoueurEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
