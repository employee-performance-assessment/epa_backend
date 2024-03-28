package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.criteria.CriteriaRequestDto;
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
    List<Criteria> create(List<CriteriaRequestDto> criteriaRequestDtoList);

    /**
     * Найти критерий оценки по его ID.
     */
    Criteria findById(Long evaluationId);

    /**
     * Получение списка всех критериев оценок.
     */
    List<Criteria> findAll();
}
