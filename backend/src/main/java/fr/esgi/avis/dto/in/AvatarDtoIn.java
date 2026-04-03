package fr.esgi.avis.dto.in;

import java.io.Serializable;

public record AvatarDtoIn(
        String nom,
        Long joueurId
) implements Serializable {}