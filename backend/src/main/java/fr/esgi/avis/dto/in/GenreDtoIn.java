package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record GenreDtoIn(
        Long id,

        @NotBlank(message = "Le nom du genre est obligatoire")
        String nom
) implements Serializable {}
