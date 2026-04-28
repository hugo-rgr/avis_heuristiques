package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AvatarDtoIn(
        @NotBlank(message = "Le nom de l'avatar est obligatoire")
        String nom,

        @NotNull(message = "L'identifiant du joueur est obligatoire")
        Long joueurId
) implements Serializable {}
