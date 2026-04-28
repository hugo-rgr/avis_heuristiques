package fr.esgi.avis.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record JeuDtoIn(
        @NotBlank(message = "Le nom du jeu est obligatoire")
        String nom,

        String description,

        Long genreId,

        String image,

        @PositiveOrZero(message = "Le prix ne peut pas être négatif")
        float prix,

        LocalDate dateDeSortie,

        Long editeurId,

        Long classificationId,

        List<Long> plateformeIds
) implements Serializable {}
