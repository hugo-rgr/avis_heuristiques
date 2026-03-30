package fr.esgi.avis.dto.out;

import fr.esgi.avis.business.Genre;

import java.io.Serializable;

public record GenreDtoOut(
        Long id,
        String nom
) implements Serializable {}