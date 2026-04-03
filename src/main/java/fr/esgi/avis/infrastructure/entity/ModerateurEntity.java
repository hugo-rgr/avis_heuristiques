package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "moderateurs")
@Getter
@Setter
@NoArgsConstructor
public class ModerateurEntity extends UtilisateurEntity {

    @Column
    private String numeroDeTelephone;
}
