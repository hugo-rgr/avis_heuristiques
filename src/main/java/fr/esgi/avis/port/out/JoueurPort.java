package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Joueur;

import java.util.Optional;

public interface JoueurPort {
    Joueur save(Joueur joueur);
    Optional<Joueur> findById(Long id);
    Optional<Joueur> findByEmail(String email);
    boolean existsByEmail(String email);
}
