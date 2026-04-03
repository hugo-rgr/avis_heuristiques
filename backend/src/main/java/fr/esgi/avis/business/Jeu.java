package fr.esgi.avis.business;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@Data
public class Jeu {
    private Long id;
    private String nom;
    private String description;
    private Genre genre;
    private String image;
    private float prix;
    private LocalDate dateDeSortie;
    private Editeur editeur;
    private Classification classification;
    private List<Plateforme> plateformes;
}
