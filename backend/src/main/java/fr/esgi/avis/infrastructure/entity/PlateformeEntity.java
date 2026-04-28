package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plateformes")
@Getter
@Setter
@NoArgsConstructor
public class PlateformeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column
    private LocalDate dateDeSortie;

    @Version
    private Long version;

    @ManyToMany(mappedBy = "plateformes", fetch = FetchType.LAZY)
    private List<JeuEntity> jeux = new ArrayList<>();
}
