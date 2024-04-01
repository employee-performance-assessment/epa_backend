package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.technology.RequestTechnologyDto;
import ru.epa.epabackend.dto.technology.ResponseTechnologyDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;

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
    ResponseTechnologyDto mapToDto(Technology technology);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology mapToEntity(RequestTechnologyDto requestTechnologyDto);

    List<ResponseTechnologyDto> mapList(List<Technology> technologies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Technology updateFields(RequestTechnologyDto requestTechnologyDto, @MappingTarget Technology oldTechnology);
}
