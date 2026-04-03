package fr.esgi.avis.business;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@Data
public class Moderateur extends Utilisateur{
    private String numeroDeTelephone;
}
