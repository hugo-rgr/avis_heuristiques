package fr.esgi.avis.dto.in;

import fr.esgi.avis.business.Classification;

import java.io.Serializable;

public record ClassificationDtoIn(
        Long id,
        String nom,
        String couleurRGB
) implements Serializable {}