package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Avis;
import fr.esgi.avis.business.Jeu;
import fr.esgi.avis.business.Joueur;
import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.dto.in.AvisDtoIn;
import fr.esgi.avis.dto.out.AvisDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvisMapper {

    @Mapping(source = "jeu.nom", target = "jeuNom")
    @Mapping(source = "joueur.pseudo", target = "joueurPseudo")
    @Mapping(source = "moderateur.pseudo", target = "moderateurPseudo")
    AvisDtoOut toDto(Avis avis);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "note", source = "dto.note")
    @Mapping(target = "dateDEnvoi", source = "dto.dateDEnvoi")
    @Mapping(target = "jeu", source = "jeu")
    @Mapping(target = "joueur", source = "joueur")
    @Mapping(target = "moderateur", source = "moderateur")
    Avis toDomain(AvisDtoIn dto, Jeu jeu, Joueur joueur, Moderateur moderateur);
}
