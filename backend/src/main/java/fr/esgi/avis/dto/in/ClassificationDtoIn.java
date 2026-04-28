package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record ClassificationDtoIn(
        Long id,

        @NotBlank(message = "Le nom de la classification est obligatoire")
        String nom,

        @NotBlank(message = "La couleur RGB est obligatoire")
        String couleurRGB
) implements Serializable {}
