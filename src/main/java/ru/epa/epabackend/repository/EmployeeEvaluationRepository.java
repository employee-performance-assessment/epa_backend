package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeEvaluationRepository extends JpaRepositoryImplementation<EmployeeEvaluation, Long> {
    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationShortDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_USER' " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationShortDto> findAllEvaluationsUsers(String email, Long questionnaireId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationShortDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.id = :evaluatedId " +
            "and e.evaluator.role = 'ROLE_USER' " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationShortDto> findAllEvaluationsUsersForAdmin(Long evaluatedId, Long questionnaireId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationShortDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_ADMIN' " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationShortDto> findAllEvaluationsAdmin(String email, Long questionnaireId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationShortDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.id = :evaluatedId " +
            "and e.evaluator.email = :emailAdmin " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationShortDto> findAllEvaluationsForAdmin(String emailAdmin, Long evaluatedId,
                                                                     Long questionnaireId);

   List<EmployeeEvaluation> findAllByEvaluatorEmailAndEvaluatedId(String email, Long evaluatedId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingFullDto(extract(month from e.createDay), round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluator.creator.id = :adminId " +
            "or e.evaluator.id = :adminId " +
            "GROUP BY e.createDay")
    List<ResponseRatingFullDto> findCommandRating(Long adminId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingFullDto(extract(month from e.createDay), round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "GROUP BY e.createDay")
    List<ResponseRatingFullDto> findPersonalRating(String email);

    @Query(value = "select e.evaluated " +
            "from EmployeeEvaluation e " +
            "where e.evaluator.email = :email ")
    List<Employee> findAllRatedByMe(String email);

    @Query(value = "select e.evaluated " +
            "from EmployeeEvaluation e " +
            "where e.evaluator.creator.email = :email ")
    List<Employee> findAllRated(String email);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation.ResponseEmployeeAssessDto(e.id, e.fullName, e.position, " +
            "ru.epa.epabackend.util.Role.ROLE_USER, q.id, q.created) " +
            "from Questionnaire q " +
            "inner join Employee e on q.author.id = e.creator.id " +
            "where e.id <> :employeeId " +
            "and q.status = 'SHARED' " +
            "and q.created > :startDate " +
            "and e.creator.id = " +
            "(select emp.creator.id " +
            "from Employee emp " +
            "where emp.id = :employeeId) " +
            "and (e.id, q.id) not in " +
            "(select ee.evaluated.id, ee.questionnaire.id " +
            "from EmployeeEvaluation as ee " +
            "where ee.evaluator.id = :employeeId " +
            "group by ee.evaluator.id, ee.evaluated.id, ee.questionnaire.id) ")
    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessment(Long employeeId, LocalDate startDate);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation.ResponseEmployeeAssessDto(e.id, e.fullName, e.position, " +
            "ru.epa.epabackend.util.Role.ROLE_ADMIN, q.id, q.created) " +
            "from Questionnaire q " +
            "inner join Employee e on q.author.id = e.creator.id " +
            "and q.status = 'SHARED' " +
            "and q.created > :startDate " +
            "and e.creator.id = :employeeId " +
            "and (e.id, q.id) not in " +
            "(select ee.evaluated.id, ee.questionnaire.id " +
            "from EmployeeEvaluation as ee " +
            "where ee.evaluator.id = :employeeId " +
            "group by ee.evaluator.id, ee.evaluated.id, ee.questionnaire.id) ")
    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessmentByAdmin(Long employeeId, LocalDate startDate);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation.ResponseEmployeeAssessDto(ee.evaluated.id, " +
            "ee.evaluated.fullName, ee.evaluated.position, ee.evaluator.role, ee.questionnaire.id, " +
            "ee.questionnaire.created) " +
            "from EmployeeEvaluation as ee " +
            "where ee.evaluator.id = :employeeId " +
            "group by ee.evaluator.id, ee.evaluated.id, ee.questionnaire.id, ee.evaluated.fullName, ee.evaluator.role, " +
            "ee.evaluated.position, ee.questionnaire.created ")
    List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesAssessed(Long employeeId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEvaluatedQuestionnaireDto(questionnaire.id idQuestionnaire, " +
            "questionnaire.created createQuestionnaire, " +
            "round(avg(score)) middleScore) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.id = :evaluatedId " +
            "and e.evaluated.creator.email = :adminEmail " +
            "GROUP BY questionnaire.id, questionnaire.created ")
    List<ResponseEvaluatedQuestionnaireDto> findListQuestionnaireByEvaluatedId(String adminEmail, Long evaluatedId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.id = :evaluatedId " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.questionnaire.id")
    ResponseRatingDto findRatingByQuestionnaireIdAndEvaluatedId(Long questionnaireId, Long evaluatedId);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :evaluatedEmail " +
            "and e.questionnaire.id = :questionnaireId " +
            "GROUP BY e.questionnaire.id")
    ResponseRatingDto findRatingByQuestionnaireIdAndEvaluatedEmail(Long questionnaireId, String evaluatedEmail);

        List<EmployeeEvaluation> findByEvaluatorIdAndEvaluatedIdAndQuestionnaireId(Long evaluatorId, Long evaluatedId,
                                                                           Long questionnaireId);
}