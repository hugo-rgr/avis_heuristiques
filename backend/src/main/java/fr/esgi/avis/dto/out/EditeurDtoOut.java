package fr.esgi.avis.dto.out;

import java.io.Serializable;

public record EditeurDtoOut(
        Long id,
        String nom
) implements Serializable {}