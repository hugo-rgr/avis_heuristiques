package fr.esgi.avis.mapper;

import fr.esgi.avis.business.Classification;
import fr.esgi.avis.dto.in.ClassificationDtoIn;
import fr.esgi.avis.dto.out.ClassificationDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassificationMapper {

    @Mapping(target = "jeux", expression = "java(java.util.Collections.emptyList())")
    Classification toDomain(ClassificationDtoIn dto);

    ClassificationDtoOut toDto(Classification classification);
}
