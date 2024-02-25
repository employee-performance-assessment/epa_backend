package ru.epa.epabackend.mapper;

import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;
import java.util.stream.Collectors;

public class TechnologyMapper {
    public static TechnologyDto toTechnologyDto(Technology technology) {
        return TechnologyDto.builder()
                .id(technology.getId())
                .name(technology.getName())
                .build();
    }

    public static Technology toTechnology(TechnologyDto technologyDto) {
        return Technology.builder()
                .id(technologyDto.getId())
                .name(technologyDto.getName())
                .build();
    }

    public static List<TechnologyDto> toTechnologyDtoList(List<Technology> technologys) {
        return technologys.stream()
                .map(TechnologyMapper::toTechnologyDto)
                .collect(Collectors.toList());
    }
}
