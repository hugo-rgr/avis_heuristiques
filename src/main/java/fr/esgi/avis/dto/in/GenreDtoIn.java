package fr.esgi.avis.dto.in;

import fr.esgi.avis.business.Genre;

import java.io.Serializable;

public record GenreDtoIn(
        Long id,
        String nom
) implements Serializable {}