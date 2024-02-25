package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;

public interface TechnologyService {
    TechnologyDto createTechnology(Technology technology);
    TechnologyDto updateTechnology(TechnologyDto technologyDto, Long technologyId);
    List<TechnologyDto> getAllTechnologys();
    Technology getTechnologyById(Long technologyId);
    TechnologyDto getTechnologyDtoById(Long technologyDtoId);
    void deleteTechnologyById(Long technologyById);


}
