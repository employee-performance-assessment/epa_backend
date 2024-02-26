package ru.epa.epabackend.mapper;

import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс TechnologyMapper содержит преобразование сущности.
 *
 * @author Артем Масалкин
 */
public class TechnologyMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    public static TechnologyDto toDto(Technology technology) {
        return TechnologyDto.builder()
                .id(technology.getId())
                .name(technology.getName())
                .build();
    }

    /**
     * Преобразование из DTO в сущность.
     */
    public static Technology toEntity(TechnologyDto technologyDto) {
        return Technology.builder()
                .id(technologyDto.getId())
                .name(technologyDto.getName())
                .build();
    }

    /**
     * Список DTO.
     */
    public static List<TechnologyDto> toTechnologyDtoList(List<Technology> technologies) {
        return technologies.stream()
                .map(TechnologyMapper::toDto)
                .collect(Collectors.toList());
    }
}
