package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.RequestEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
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
    List<EmployeeEvaluation> create(String email,
                                    Long evaluatedId,
                                    List<RequestEmployeeEvaluationDto> evaluationListDto);

    /**
     * Найти оценку сотрудника по идентификатору.
     */
    EmployeeEvaluation findById(Long employeeEvaluationId);

    /**
     * Получение списка всех оценок коллег.
     */
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsUsers(String email);

    /**
     * Получение списка оценок руководителя.
     */
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdmin(String email);

    /**
     * Получение рейтинга за определенный период сотрудника от всего коллектива.
     */
    ResponseRatingDto findFullRating(String email, LocalDate startDay, LocalDate endDay);

    /**
     * Получение рейтинга за определенный период сотрудника только от руководителя.
     */
    ResponseRatingDto findRatingByAdmin(String email, LocalDate startDay, LocalDate endDay);
}
