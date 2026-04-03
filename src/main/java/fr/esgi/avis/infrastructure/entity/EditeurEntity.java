package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "editeurs")
@Getter
@Setter
@NoArgsConstructor
public class EditeurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "editeur", fetch = FetchType.LAZY)
    private List<JeuEntity> jeux = new ArrayList<>();
}
