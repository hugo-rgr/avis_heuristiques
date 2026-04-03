package fr.esgi.avis.dto.out;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record JeuDtoOut(
        Long id,
        String nom,
        String description,
        String image,
        float prix,
        LocalDate dateDeSortie,
        String genreNom,
        String editeurNom,
        String classificationNom,
        List<String> plateformes
) implements Serializable {}