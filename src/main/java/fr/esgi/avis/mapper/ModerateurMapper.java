package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Moderateur;
import fr.esgi.avis.dto.out.ModerateurDtoOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModerateurMapper {

    ModerateurDtoOut toDto(Moderateur moderateur);
}
