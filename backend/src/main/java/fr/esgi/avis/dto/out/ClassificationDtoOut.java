package fr.esgi.avis.dto.out;

import java.io.Serializable;

public record ClassificationDtoOut(
        Long id,
        String nom,
        String couleurRGB
) implements Serializable {}