package fr.esgi.avis.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Data
public class Avis {
    private Long id;
    private String description;
    private Jeu jeu;
    private Joueur joueur;
    private float note;
    private Moderateur moderateur;
    private LocalDate dateDEnvoi;
}
