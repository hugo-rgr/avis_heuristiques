package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Genre;

import java.util.List;
import java.util.Optional;

public interface GenrePort {
    Genre save(Genre genre);
    Optional<Genre> findById(Long id);
    List<Genre> findAll();
    void deleteById(Long id);
}
