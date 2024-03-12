package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
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
    TechnologyResponseDto create(TechnologyRequestDto technologyDto);

    /**
     * Получение технологии по идентификатору.
     */
    Technology findById(Long technologyId);

    /**
     * Обновление технологии.
     */
    TechnologyResponseDto update(TechnologyRequestDto technologyDto, Long technologyId);

    /**
     * Получение списка всех технологий.
     */
    List<TechnologyResponseDto> findAll();

    /**
     * Удаление технологии по идентификатору.
     */
    void delete(Long technologyById);
}
