package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record EditeurDtoIn(
        Long id,

        @NotBlank(message = "Le nom de l'éditeur est obligatoire")
        String nom
) implements Serializable {}
