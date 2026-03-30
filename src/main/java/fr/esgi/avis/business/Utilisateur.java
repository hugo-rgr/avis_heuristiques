package fr.esgi.avis.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Data
public class Utilisateur {

    private Long id;
    private String pseudo;
    private String email;
    private String motDePasse;
}
