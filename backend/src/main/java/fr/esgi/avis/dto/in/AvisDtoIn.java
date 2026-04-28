package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record AvisDtoIn(
        @NotBlank(message = "La description est obligatoire")
        String description,

        @NotNull(message = "L'identifiant du jeu est obligatoire")
        Long jeuId,

        @NotNull(message = "L'identifiant du joueur est obligatoire")
        Long joueurId,

        @Min(value = 0, message = "La note doit être au moins 0")
        @Max(value = 10, message = "La note ne peut pas dépasser 10")
        float note,

        Long moderateurId,

        LocalDate dateDEnvoi
) implements Serializable {}
