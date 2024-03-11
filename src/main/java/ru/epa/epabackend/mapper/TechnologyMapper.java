package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.technology.TechnologyCreateUpdateFindAllResponseDto;
import ru.epa.epabackend.dto.technology.TechnologyCreateUpdateRequestDto;
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
    TechnologyCreateUpdateFindAllResponseDto mapToDto(Technology technology);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology mapToEntity(TechnologyCreateUpdateRequestDto technologyDto);
}
