package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс EvaluationService содержит методы действий с критериями оценок для админа
 *
 * @author Михаил Безуглов
 */
public interface CriteriaService {

    /**
     * Создание критерия оценки
     */
    List<Criteria> create(List<RequestCriteriaDto> requestCriteriaDtoList);

    /**
     * Получение критерия оценки по id
     */
    Criteria findById(Long evaluationId);

    /**
     * Получение списка стандартных критериев
     */
    List<Criteria> findDefault();

    List<Criteria> findExistentAndSaveNonExistentCriterias(Set<String> criterias);
}
