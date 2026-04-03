package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Plateforme;
import fr.esgi.avis.dto.in.PlatefomeDtoIn;
import fr.esgi.avis.dto.out.PlatefomeDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlateformeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jeux", expression = "java(java.util.Collections.emptyList())")
    Plateforme toDomain(PlatefomeDtoIn dto);

    @Mapping(target = "jeuxIds", expression = "java(java.util.Collections.emptyList())")
    PlatefomeDtoOut toDto(Plateforme plateforme);
}
