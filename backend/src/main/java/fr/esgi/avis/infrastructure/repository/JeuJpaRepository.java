package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.JeuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JeuJpaRepository extends JpaRepository<JeuEntity, Long> {
    List<JeuEntity> findAllByGenreId(Long genreId);
    Page<JeuEntity> findAllByGenreId(Long genreId, Pageable pageable);
    List<JeuEntity> findAllByEditeurId(Long editeurId);
    Page<JeuEntity> findAllByEditeurId(Long editeurId, Pageable pageable);
}
