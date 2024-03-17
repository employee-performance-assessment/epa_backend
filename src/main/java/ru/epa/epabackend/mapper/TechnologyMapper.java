package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
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
    @Mapping(target = "employees", ignore = true)
    Technology mapToEntity(TechnologyRequestDto technologyDto);

    default List<TechnologyResponseDto> mapList(List<Technology> technologies) {
        return technologies.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
