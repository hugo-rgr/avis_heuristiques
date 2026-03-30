package fr.esgi.avis.dto.out;

import java.io.Serializable;

public record AvatarDtoOut(
        Long id,
        String nom,
        Long joueurId,
        String joueurPseudo
) implements Serializable {}

