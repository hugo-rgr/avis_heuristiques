package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Avis;

import java.util.List;
import java.util.Optional;

public interface AvisPort {
    Avis save(Avis avis);
    Optional<Avis> findById(Long id);
    List<Avis> findAll();
    List<Avis> findAllByJeuId(Long jeuId);
    List<Avis> findAllByJoueurId(Long joueurId);
    void deleteById(Long id);
}
