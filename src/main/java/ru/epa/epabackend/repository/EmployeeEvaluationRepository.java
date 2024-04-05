package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationShortDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingFullDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;

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
}
