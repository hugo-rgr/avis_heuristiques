package fr.esgi.avis.dto.in;

import fr.esgi.avis.business.Avis;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Avis}
 */
public record AvisDtoIn(Long id, String description, Long jeuId, Long joueurId, float note, Long moderateurId,
                        LocalDate dateDEnvoi) implements Serializable {
}