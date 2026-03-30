package fr.esgi.avis.dto.in;

import fr.esgi.avis.business.Editeur;

import java.io.Serializable;

public record EditeurDtoIn(
        Long id,
        String nom
) implements Serializable {}