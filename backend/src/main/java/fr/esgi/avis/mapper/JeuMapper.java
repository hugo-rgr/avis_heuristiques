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
    JeuDtoOut toDto(Jeu jeu);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "nom", source = "dto.nom")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "image", source = "dto.image")
    @Mapping(target = "prix", source = "dto.prix")
    @Mapping(target = "dateDeSortie", source = "dto.dateDeSortie")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "editeur", source = "editeur")
    @Mapping(target = "classification", source = "classification")
    @Mapping(target = "plateformes", source = "plateformes")
    Jeu toDomain(JeuDtoIn dto, Genre genre, Editeur editeur, Classification classification, List<Plateforme> plateformes);

    default String plateformeToNom(Plateforme plateforme) {
        return plateforme != null ? plateforme.getNom() : null;
    }
}
