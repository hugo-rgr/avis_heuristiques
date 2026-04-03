package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreJpaRepository extends JpaRepository<GenreEntity, Long> {
}
