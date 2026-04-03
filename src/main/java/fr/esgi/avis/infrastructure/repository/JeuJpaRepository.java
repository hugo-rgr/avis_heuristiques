package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.JeuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JeuJpaRepository extends JpaRepository<JeuEntity, Long> {
    List<JeuEntity> findAllByGenreId(Long genreId);
    List<JeuEntity> findAllByEditeurId(Long editeurId);
}
