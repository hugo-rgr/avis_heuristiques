package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.infrastructure.entity.AvisEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AvisJpaRepository extends JpaRepository<AvisEntity, Long> {
    List<AvisEntity> findAllByJeuId(Long jeuId);
    Page<AvisEntity> findAllByJeuId(Long jeuId, Pageable pageable);
    List<AvisEntity> findAllByJoueurId(Long joueurId);
    Page<AvisEntity> findAllByJoueurId(Long joueurId, Pageable pageable);

    @Query("SELECT AVG(a.note) FROM AvisEntity a WHERE a.jeu.id = :jeuId")
    Optional<Double> findNoteMoyenneByJeuId(@Param("jeuId") Long jeuId);
}
