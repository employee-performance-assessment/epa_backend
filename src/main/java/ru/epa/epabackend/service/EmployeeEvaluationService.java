package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.security.Principal;
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
     * Получение списка оцененных коллег.
     */
    List<Employee> findAllRatedByMe(String email);

    /**
     * Получение списка оцененных коллег для админа.
     */
    List<Employee> findAllRated(String email);

    /**
     * Получение оценок и рекомендации для сотрудника.
     */
    ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireId(String email, Long questionnaireId);

    /**
     * Получение оценок и рекомендации для админа.
     */
    ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireIdForAdmin(String adminEmail,
    Long questionnaireId, Long evaluatedId);

    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessment(String email,String text, LocalDate from,
                                                                             LocalDate to);

    /**
     * Получение оценок коллег по id анкеты.
     */
    List<ResponseMyEvaluationsDto> findAllMyEvaluationsByEvaluatedId(String email, Long evaluatedId);

    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesAssessed(String email, String text, LocalDate from,
                                                                        LocalDate to);

    /**
     * Получение списка анкет в которых оценен сотрудник с id.
     */
    List<ResponseEvaluatedQuestionnaireDto> findAllQuestionnaireByEvaluatedId(String adminEmail, Long evaluatedId);

    List<ResponseEmployeeEvaluationShortDto> findQuestionnaireScores(String email, Long questionnaireId, Long evaluatedId);

    ResponseAdminEvaluationDto findAssessedQuestionnaireByAdmin(String name, Long questionnaireId, Long evaluatedId);

    Double findAverageRatingByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd);

    Double findAverageRatingByAdmin(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd);
}
