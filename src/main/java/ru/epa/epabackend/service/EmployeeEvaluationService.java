package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.model.EmployeeEvaluation;

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
    EmployeeEvaluation create(Long employeeId, Long appraiserId, EmployeeEvaluationRequestDto employeeEvaluationDto);

    /**
     * Найти оценку сотрудника по идентификатору.
     */
    EmployeeEvaluation findById(Long employeeEvaluationId);

    /**
     * Получение списка всех оценок.
     */
    List<EmployeeEvaluation> findAllByAppraiserId(Long appraiserId);

    /**
     * Удаление оценки.
     */
    void delete(Long employeeEvaluationId);
}
