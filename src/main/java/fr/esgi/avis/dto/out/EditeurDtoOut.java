package fr.esgi.avis.dto.out;

import fr.esgi.avis.business.Editeur;

import java.io.Serializable;

public record EditeurDtoOut(
        Long id,
        String nom
) implements Serializable {}