package fr.esgi.avis.business;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Data

abstract class Utilisateur {
    private Long id;
    private String pseudo;
    private String email;
    private String motDePasse;
}
