package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

public record PlatefomeDtoIn(
        @NotBlank(message = "Le nom de la plateforme est obligatoire")
        String nom,

        LocalDate dateDeSortie
) implements Serializable {}
