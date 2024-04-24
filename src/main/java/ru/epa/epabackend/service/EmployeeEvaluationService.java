package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Questionnaire;

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
     * Сохранение оценки сотрудника от его руководителя или коллег
     */
    List<EmployeeEvaluation> create(String email, Long evaluatedId, Long questionnaireId,
                                    List<RequestEmployeeEvaluationDto> evaluationListDto);

    /**
     * Получение командного рейтинга за каждый месяц указанного года
     */
    List<ResponseRatingFullDto> findCommandRating(String email, Integer year);

    /**
     * Получение личного рейтинга за каждый месяц
     */
    List<ResponseRatingFullDto> findPersonalRating(String email, Integer year);

    /**
     * Получение руководителем рейтинга сотрудника за каждый месяц указанного года
     */
    List<ResponseRatingFullDto> findPersonalRatingAdmin(String email, Long evaluatedId, Integer year);

    /**
     * Получение оценок и рекомендации для сотрудника
     */
    ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireId(String email, Long questionnaireId);

    /**
     * Получение оценок и рекомендации для админа
     */
    ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireIdForAdmin(String adminEmail,
    Long questionnaireId, Long evaluatedId);

    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessment(String email,String text, LocalDate from,
                                                                             LocalDate to);

    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesAssessed(String email, String text, LocalDate from,
                                                                        LocalDate to);

    /**
     * Получение списка анкет в которых оценен сотрудник с id
     */
    List<ResponseEvaluatedQuestionnaireDto> findAllQuestionnaireByEvaluatedId(String adminEmail, Long evaluatedId,
                                                                              Integer stars, LocalDate from, LocalDate to);

    /**
     * Получение списка анкет в которых оценен сотрудник с email
     */
    List<ResponseEvaluatedQuestionnaireDto> findAllQuestionnaireByEvaluatedEmail(String email, Integer stars, LocalDate from, LocalDate to);

    List<ResponseEmployeeEvaluationShortDto> findQuestionnaireScores(String email, Long questionnaireId, Long evaluatedId);

    ResponseAdminEvaluationDto findAssessedQuestionnaireByAdmin(String name, Long questionnaireId, Long evaluatedId);

    Double findAverageRatingByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd);

    Double findAverageRatingByAdmin(String email, Long employeeId, LocalDate rangeStart, LocalDate rangeEnd);

    void checkQuestionnaireForEvaluator(Questionnaire questionnaire, Employee evaluator);
}
