package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;
import java.util.stream.Collectors;

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

    List<TechnologyResponseDto> mapList(List<Technology> technologies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology updateFields(TechnologyRequestDto technologyDto, @MappingTarget Technology oldTechnology);
}
