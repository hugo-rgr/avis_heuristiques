package fr.esgi.avis.dto.in;

import java.io.Serializable;

public record GenreDtoIn(
        Long id,
        String nom
) implements Serializable {}