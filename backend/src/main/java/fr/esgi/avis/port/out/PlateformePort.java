package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Plateforme;

import java.util.List;
import java.util.Optional;

public interface PlateformePort {
    Plateforme save(Plateforme plateforme);
    Optional<Plateforme> findById(Long id);
    List<Plateforme> findAll();
    List<Plateforme> findAllByIds(List<Long> ids);
    void deleteById(Long id);
}
