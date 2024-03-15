package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Technology;

/**
 * Класс TechnologyMapper содержит преобразование сущности.
 *
 * @author Артем Масалкин
 */
@Mapper(componentModel = "spring")
public interface TechnologyMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    TechnologyResponseDto mapToDto(Technology technology);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology mapToEntity(TechnologyRequestDto technologyDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology updateFields(TechnologyRequestDto technologyDto, @MappingTarget Technology oldTechnology);
}
