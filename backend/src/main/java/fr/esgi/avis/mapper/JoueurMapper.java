package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.JoueurDtoIn;
import fr.esgi.avis.dto.out.JoueurDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JoueurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avis", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "avatar", ignore = true)
    Joueur toDomain(JoueurDtoIn dto);

    @Mapping(source = "avatar.nom", target = "avatarNom")
    JoueurDtoOut toDto(Joueur joueur);
}
