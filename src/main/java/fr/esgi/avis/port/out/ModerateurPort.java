package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Moderateur;

import java.util.Optional;

public interface ModerateurPort {
    Moderateur save(Moderateur moderateur);
    Optional<Moderateur> findById(Long id);
    Optional<Moderateur> findByEmail(String email);
}
