package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "joueurs")
@Getter
@Setter
@NoArgsConstructor
public class JoueurEntity extends UtilisateurEntity {

    @Column
    private LocalDate dateDeNaissance;

    @OneToOne(mappedBy = "joueur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AvatarEntity avatar;

    @OneToMany(mappedBy = "joueur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvisEntity> avis = new ArrayList<>();
}
