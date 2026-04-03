package fr.esgi.avis.dto.out;

import java.io.Serializable;

public record GenreDtoOut(
        Long id,
        String nom
) implements Serializable {}