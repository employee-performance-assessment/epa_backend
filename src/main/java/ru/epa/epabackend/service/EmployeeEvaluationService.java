package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationResponseDto;
import ru.epa.epabackend.dto.evaluation.RatingResponseDto;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс EmployeeEvaluationService содержит методы действий с оценками руководителя сотрудников или
 * оценками сотрудников друг друга.
 *
 * @author Михаил Безуглов
 */
public interface EmployeeEvaluationService {

    /**
     * Сохранение оценки сотрудника от его руководителя или коллег.
     */
    List<EmployeeEvaluation> create(Long evaluatorId, Long evaluatedId,
                                    List<EmployeeEvaluationRequestDto> evaluationListDto);

    /**
     * Найти оценку сотрудника по идентификатору.
     */
    EmployeeEvaluation findById(Long employeeEvaluationId);

    /**
     * Получение списка всех оценок коллег.
     */
    List<EmployeeEvaluationResponseDto> findAllEvaluationsUsers(Long evaluatedId);

    /**
     * Получение списка оценок руководителя.
     */
    List<EmployeeEvaluationResponseDto> findAllEvaluationsAdmin(Long evaluatedId);

    /**
     * Получение рейтинга за определенный период сотрудника от всего коллектива.
     */
    RatingResponseDto findFullRating(Long evaluatedId, LocalDate startDay, LocalDate endDay);

    /**
     * Получение рейтинга за определенный период сотрудника только от руководителя.
     */
    RatingResponseDto findRatingByAdmin(Long evaluatedId, LocalDate startDay, LocalDate endDay);
}
