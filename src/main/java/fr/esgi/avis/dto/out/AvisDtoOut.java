package fr.esgi.avis.dto.out;

import fr.esgi.avis.business.Avis;

import java.io.Serializable;
import java.time.LocalDate;

public record AvisDtoOut(
        Long id,
        String description,
        float note,
        String jeuNom,
        String joueurPseudo,
        String moderateurPseudo,
        LocalDate dateDEnvoi
) implements Serializable {}