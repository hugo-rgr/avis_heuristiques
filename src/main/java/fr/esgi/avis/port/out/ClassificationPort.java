package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Classification;

import java.util.List;
import java.util.Optional;

public interface ClassificationPort {
    Classification save(Classification classification);
    Optional<Classification> findById(Long id);
    List<Classification> findAll();
    void deleteById(Long id);
}
