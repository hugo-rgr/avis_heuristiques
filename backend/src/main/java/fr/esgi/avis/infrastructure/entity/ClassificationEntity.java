package fr.esgi.avis.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classifications")
@Getter
@Setter
@NoArgsConstructor
public class ClassificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private String couleurRGB;

    @Version
    private Long version;

    @OneToMany(mappedBy = "classification", fetch = FetchType.LAZY)
    private List<JeuEntity> jeux = new ArrayList<>();
}
