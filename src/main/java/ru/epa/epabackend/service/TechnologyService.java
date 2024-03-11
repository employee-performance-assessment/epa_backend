package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.technology.TechnologyCreateUpdateFindAllResponseDto;
import ru.epa.epabackend.dto.technology.TechnologyCreateUpdateRequestDto;
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
    TechnologyCreateUpdateFindAllResponseDto create(TechnologyCreateUpdateRequestDto technologyDto);

    /**
     * Получение технологии по идентификатору.
     */
    Technology findById(Long technologyId);

    /**
     * Обновление технологии.
     */
    TechnologyCreateUpdateFindAllResponseDto update(TechnologyCreateUpdateRequestDto technologyDto, Long technologyId);

    /**
     * Получение списка всех технологий.
     */
    List<TechnologyCreateUpdateFindAllResponseDto> findAll();

    /**
     * Удаление технологии по идентификатору.
     */
    void delete(Long technologyById);
}
