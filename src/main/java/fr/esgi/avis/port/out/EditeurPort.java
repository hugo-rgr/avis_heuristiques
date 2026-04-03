package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Editeur;

import java.util.List;
import java.util.Optional;

public interface EditeurPort {
    Editeur save(Editeur editeur);
    Optional<Editeur> findById(Long id);
    List<Editeur> findAll();
    void deleteById(Long id);
}
