package fr.esgi.avis.dto.out;

import java.io.Serializable;
import java.time.LocalDate;

public record JoueurDtoOut(Long id, String pseudo, String email, LocalDate dateDeNaissance, String avatarNom) implements Serializable {
}

