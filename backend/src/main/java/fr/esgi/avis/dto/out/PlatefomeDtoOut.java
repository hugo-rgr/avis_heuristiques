package fr.esgi.avis.dto.out;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record PlatefomeDtoOut(
        Long id,
        String nom,
        LocalDate dateDeSortie,
        List<Long> jeuxIds
) implements Serializable {}

