package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.dto.in.EditeurDtoIn;
import fr.esgi.avis.dto.out.EditeurDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditeurMapper {

    @Mapping(target = "jeux", expression = "java(java.util.Collections.emptyList())")
    Editeur toDomain(EditeurDtoIn dto);

    EditeurDtoOut toDto(Editeur editeur);
}
