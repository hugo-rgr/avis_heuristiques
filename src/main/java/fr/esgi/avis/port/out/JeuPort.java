package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Jeu;

import java.util.List;
import java.util.Optional;

public interface JeuPort {
    Jeu save(Jeu jeu);
    Optional<Jeu> findById(Long id);
    List<Jeu> findAll();
    List<Jeu> findAllByGenreId(Long genreId);
    List<Jeu> findAllByEditeurId(Long editeurId);
    void deleteById(Long id);
}
