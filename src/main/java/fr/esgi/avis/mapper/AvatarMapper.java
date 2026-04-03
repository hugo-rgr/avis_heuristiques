package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Avatar;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.dto.in.AvatarDtoIn;
import fr.esgi.avis.dto.out.AvatarDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvatarMapper {

    @Mapping(source = "joueur.id", target = "joueurId")
    @Mapping(source = "joueur.pseudo", target = "joueurPseudo")
    AvatarDtoOut toDto(Avatar avatar);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "joueur", source = "joueur")
    Avatar toDomain(AvatarDtoIn dto, Joueur joueur);
}
