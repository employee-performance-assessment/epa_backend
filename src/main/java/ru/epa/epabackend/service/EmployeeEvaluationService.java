package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.*;
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
    List<EmployeeEvaluation> create(String email, Long evaluatedId, Long questionnaireId,
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

    /**
     * Получение списка оцененных коллег по ID анкеты.
     */
    List<ResponseMyEvaluationsDto> findAllMyEvaluationsById(String email, Long questionnaireId);

    /**
     * Получение списка оцененных коллег по всем анкетам.
     */
    List<ResponseMyEvaluationsDto> findAllMyEvaluations(String email);

    /**
     * Получение командного рейтинга за каждый месяц.
     */
    List<ResponseRatingFullDto> findCommandRating(String email);

    /**
     * Получение личного рейтинга за каждый месяц.
     */
    List<ResponseRatingFullDto> findPersonalRating(String email);

    /**
     * Получение рейтинга каждого сотрудника за каждый месяц для админа.
     */
    List<ResponsePersonalRatingDto> findPersonalRatingAdmin(String email);

    /**
     * Получение командного рейтинга за каждый месяц для админа.
     */
    List<ResponseRatingFullDto> findCommandRatingForAdmin(String email);
}
