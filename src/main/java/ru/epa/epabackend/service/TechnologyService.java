package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
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
    Technology create(TechnologyRequestDto technologyDto);

    /**
     * Получение технологии по идентификатору.
     */
    Technology findById(Long technologyId);

    /**
     * Обновление технологии.
     */
    Technology update(TechnologyRequestDto technologyDto, Long technologyId);

    /**
     * Получение списка всех технологий.
     */
    List<Technology> findAll();

    /**
     * Удаление технологии по идентификатору.
     */
    void delete(Long technologyById);
}
