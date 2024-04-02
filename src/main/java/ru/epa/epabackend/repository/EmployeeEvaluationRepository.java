package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingFullDto;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeEvaluationRepository extends JpaRepositoryImplementation<EmployeeEvaluation, Long> {
    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_USER' " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsUsers(String email);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationDto(criteria.name, " +
            "round(avg(score))) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_ADMIN' " +
            "GROUP BY e.criteria.name," +
            " e.score")
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdmin(String email);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.createDay BETWEEN :startDay AND :endDay ")
    ResponseRatingDto findFullRating(String email, LocalDate startDay, LocalDate endDay);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score)) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_ADMIN' " +
            "and e.createDay BETWEEN :startDay AND :endDay ")
    ResponseRatingDto findRatingByAdmin(String email, LocalDate startDay, LocalDate endDay);

    List<EmployeeEvaluation> findAllByEvaluatorEmailAndQuestionnaireId(String email, Long questionnaireId);

    List<EmployeeEvaluation> findAllByEvaluatorEmail(String email);

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
}
