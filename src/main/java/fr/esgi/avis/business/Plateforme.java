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
public class Plateforme {

    private Long id;
    private String nom;
    private List<Jeu> jeux;
    private LocalDate dateDeSortie;

}
