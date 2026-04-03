package fr.esgi.avis.business;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Data
public class Avatar {

    private Long id;
    private String nom;
    private Joueur joueur;

}
