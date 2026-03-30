package fr.esgi.avis.dto.out;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record JeuDtoOut(Long id, String nom, String description, String genreNom, String image, float prix, LocalDate dateDeSortie,
                      String editeurNom, String classificationNom, List<String> plateformeNoms) implements Serializable {
}

