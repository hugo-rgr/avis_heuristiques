package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "avis")
@Getter
@Setter
@NoArgsConstructor
public class AvisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private float note;

    @Column
    private LocalDate dateDEnvoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jeu_id", nullable = false)
    private JeuEntity jeu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joueur_id", nullable = false)
    private JoueurEntity joueur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderateur_id")
    private ModerateurEntity moderateur;
}
