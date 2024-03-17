package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.model.EmployeeEvaluation;

/**
 * Интерфейс EmployeeEvaluationService содержит методы действий с оценками руководителя сотрудников или
 * оценками сотрудников друг друга.
 *
 * @author Михаил Безуглов
 */
public interface EmployeeEvaluationService {

    /**
     * Сохранение оценки сотрудника от его руководителя или коллег
     */
    EmployeeEvaluation create(EmployeeEvaluationRequestDto employeeEvaluationDto);



}
