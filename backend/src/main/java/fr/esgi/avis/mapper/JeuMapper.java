package fr.esgi.avis.mapper;

import fr.esgi.avis.business.*;
import fr.esgi.avis.dto.in.JeuDtoIn;
import fr.esgi.avis.dto.out.JeuDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JeuMapper {

    @Mapping(source = "genre.nom", target = "genreNom")
    @Mapping(source = "editeur.nom", target = "editeurNom")
    @Mapping(source = "classification.nom", target = "classificationNom")
    @Mapping(source = "plateformes", target = "plateformes")
    @Mapping(source = "noteMoyenne", target = "noteMoyenne")
    JeuDtoOut toDto(Jeu jeu);

    /** Chemin CRÉATION : id ignoré (auto-généré par la base) */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nom", source = "dto.nom")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "image", source = "dto.image")
    @Mapping(target = "prix", source = "dto.prix")
    @Mapping(target = "dateDeSortie", source = "dto.dateDeSortie")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "editeur", source = "editeur")
    @Mapping(target = "classification", source = "classification")
    @Mapping(target = "plateformes", source = "plateformes")
    @Mapping(target = "noteMoyenne", ignore = true)
    Jeu toDomain(JeuDtoIn dto, Genre genre, Editeur editeur, Classification classification, List<Plateforme> plateformes);

    /** Chemin MISE À JOUR : id propagé depuis le path variable */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "dto.nom")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "image", source = "dto.image")
    @Mapping(target = "prix", source = "dto.prix")
    @Mapping(target = "dateDeSortie", source = "dto.dateDeSortie")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "editeur", source = "editeur")
    @Mapping(target = "classification", source = "classification")
    @Mapping(target = "plateformes", source = "plateformes")
    @Mapping(target = "noteMoyenne", ignore = true)
    Jeu toDomain(Long id, JeuDtoIn dto, Genre genre, Editeur editeur, Classification classification, List<Plateforme> plateformes);

    default String plateformeToNom(Plateforme plateforme) {
        return plateforme != null ? plateforme.getNom() : null;
    }
}
