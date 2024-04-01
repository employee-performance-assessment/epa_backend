package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;

/**
 * Интерфейс EvaluationService содержит методы действий с критериями оценок для администратора.
 *
 * @author Михаил Безуглов
 */
public interface CriteriaService {

    /**
     * Создание критерия оценки.
     */
    List<Criteria> create(List<RequestCriteriaDto> requestCriteriaDtoList);

    /**
     * Найти критерий оценки по его ID.
     */
    Criteria findById(Long evaluationId);

    /**
     * Получение списка всех критериев оценок.
     */
    List<Criteria> findAll();

    /**
     * Удаление критерия оценки.
     */
    void delete(Long evaluationId);

    /**
     * Получение списка стандартных критериев
     */
    List<Criteria> findDefault();

    boolean isNameExists(String name);

    Criteria findByName(String name);

    List<Criteria> findExistentAndSaveNonExistentCriterias(List<RequestCriteriaDto> criterias);
}
