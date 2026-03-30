package fr.esgi.avis.dto.in;

import fr.esgi.avis.business.Jeu;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record JeuDtoIn(
        Long id,
        String nom,
        String description,
        Long genreId,
        String image,
        float prix,
        LocalDate dateDeSortie,
        Long editeurId,
        Long classificationId,
        List<Long> plateformeIds
) implements Serializable {}