package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.EvaluationRequestDto;
import ru.epa.epabackend.model.Evaluation;

import java.util.List;

/**
 * Интерфейс EvaluationService содержит методы действий с оценками для администратора.
 *
 * @author Михаил Безуглов
 */
public interface EvaluationService {

    /**
     * Создание оценки
     */
    Evaluation create(EvaluationRequestDto evaluationDto);

    /**
     * Найти оценку по идентификатору
     */
    Evaluation findById(Long evaluationId);

    /**
     * Получение списка всех оценок
     */
    List<Evaluation> findAll();

    /**
     * Удаление оценки
     */
    void delete(Long evaluationId);
}
