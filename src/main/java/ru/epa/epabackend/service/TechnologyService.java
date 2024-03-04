package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.technology.TechnologyDto;
import ru.epa.epabackend.model.Technology;

import java.util.List;

/**
 * Интерфейс TechnologyService содержит методы действий с технологией.
 *
 * @author Артем Масалкин
 */
public interface TechnologyService {

    /**
     * Добавление технологии.
     */
    TechnologyDto createTechnology(TechnologyDto technologyDto);

    /**
     * Получение технологии по идентификатору.
     */
    Technology getTechnologyById(Long technologyId);

    /**
     * Обновление технологии.
     */
    TechnologyDto updateTechnology(TechnologyDto technologyDto, Long technologyId);

    /**
     * Получение списка всех технологий.
     */
    List<TechnologyDto> getAllTechnologies();

    /**
     * Удаление технологии по идентификатору.
     */
    void deleteTechnologyById(Long technologyById);
}
