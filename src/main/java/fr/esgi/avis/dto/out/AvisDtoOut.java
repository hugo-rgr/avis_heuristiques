package fr.esgi.avis.dto.out;

import java.io.Serializable;
import java.time.LocalDate;

public record AvisDtoOut(Long id, String description, float note, LocalDate dateDEnvoi, Long jeuId, String jeuNom, Long joueurId, String joueurPseudo) implements Serializable {
}

