package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Jeu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JeuPort {
    Jeu save(Jeu jeu);
    Optional<Jeu> findById(Long id);
    List<Jeu> findAll();
    Page<Jeu> findAll(Pageable pageable);
    List<Jeu> findAllByGenreId(Long genreId);
    Page<Jeu> findAllByGenreId(Long genreId, Pageable pageable);
    List<Jeu> findAllByEditeurId(Long editeurId);
    Page<Jeu> findAllByEditeurId(Long editeurId, Pageable pageable);
    void deleteById(Long id);
}
