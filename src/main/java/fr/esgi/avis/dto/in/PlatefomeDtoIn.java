package fr.esgi.avis.dto.in;

import java.io.Serializable;
import java.time.LocalDate;

public record PlatefomeDtoIn(
        String nom,
        LocalDate dateDeSortie
) implements Serializable {}

