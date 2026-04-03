package fr.esgi.avis.dto.in;

import java.io.Serializable;

public record EditeurDtoIn(
        Long id,
        String nom
) implements Serializable {}