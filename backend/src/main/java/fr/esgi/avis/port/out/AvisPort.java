package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Avis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AvisPort {
    Avis save(Avis avis);
    Optional<Avis> findById(Long id);
    List<Avis> findAll();
    Page<Avis> findAll(Pageable pageable);
    List<Avis> findAllByJeuId(Long jeuId);
    Page<Avis> findAllByJeuId(Long jeuId, Pageable pageable);
    List<Avis> findAllByJoueurId(Long joueurId);
    Page<Avis> findAllByJoueurId(Long joueurId, Pageable pageable);
    void deleteById(Long id);
    Optional<Double> findNoteMoyenneByJeuId(Long jeuId);
}
