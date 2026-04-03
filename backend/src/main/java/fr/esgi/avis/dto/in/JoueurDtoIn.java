package fr.esgi.avis.dto.in;

import java.time.LocalDate;

public record JoueurDtoIn(String pseudo, String email, String motDePasse, LocalDate dateDeNaissance) {
}

