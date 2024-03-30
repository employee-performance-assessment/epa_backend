package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс EvaluationService содержит методы действий с критериями оценок для администратора.
 *
 * @author Михаил Безуглов
 */
public interface CriteriaService {

    /**
     * Создание критерия оценки.
     */
    Criteria create(CriteriaRequestDto criteriaRequestDto);

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

    List<Criteria> findExistentAndSaveNonExistentCriterias(List<CriteriaRequestDto> criterias);
}
