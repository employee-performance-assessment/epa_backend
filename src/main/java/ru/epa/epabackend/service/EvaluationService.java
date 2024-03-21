package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.EvaluationRequestDto;
import ru.epa.epabackend.model.Evaluation;

import java.util.List;

/**
 * Интерфейс EvaluationService содержит методы действий с критериями оценок для администратора.
 *
 * @author Михаил Безуглов
 */
public interface EvaluationService {

    /**
     * Создание критерия оценки.
     */
    Evaluation create(EvaluationRequestDto evaluationRequestDto);

    /**
     * Найти критерий оценки по его ID.
     */
    Evaluation findById(Long evaluationId);

    /**
     * Получение списка всех критериев оценок.
     */
    List<Evaluation> findAll();

    /**
     * Удаление критерия оценки.
     */
    void delete(Long evaluationId);
}
