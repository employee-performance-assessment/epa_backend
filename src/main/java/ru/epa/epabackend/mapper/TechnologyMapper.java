package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.technology.TechnologyDto;
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
    TechnologyDto mapToDto(Technology technology);

    /**
     * Преобразование из DTO в сущность.
     */
    Technology mapToEntity(TechnologyDto technologyDto);
}
