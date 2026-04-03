package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Genre;
import fr.esgi.avis.dto.in.GenreDtoIn;
import fr.esgi.avis.dto.out.GenreDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mapping(target = "jeux", expression = "java(java.util.Collections.emptyList())")
    Genre toDomain(GenreDtoIn dto);

    GenreDtoOut toDto(Genre genre);
}
