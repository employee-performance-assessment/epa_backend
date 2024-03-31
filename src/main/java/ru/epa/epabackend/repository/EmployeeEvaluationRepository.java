package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeEvaluationRepository extends JpaRepositoryImplementation<EmployeeEvaluation, Long> {
    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationDto(criteria.name, " +
            "round(avg(score), 1)) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_USER' " +
            "GROUP BY e.criteria.name")
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsUsers(String email);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseEmployeeEvaluationDto(criteria.name, " +
            "round(avg(score), 1)) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_ADMIN' " +
            "GROUP BY e.criteria.name," +
            " e.score")
    List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdmin(String email);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score), 1) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.createDay BETWEEN :startDay AND :endDay ")
    ResponseRatingDto findFullRating(String email, LocalDate startDay, LocalDate endDay);

    @Query(value = "select new ru.epa.epabackend.dto.evaluation" +
            ".ResponseRatingDto(round(avg(score), 1) rating) " +
            "from EmployeeEvaluation e " +
            "where e.evaluated.email = :email " +
            "and e.evaluator.role = 'ROLE_ADMIN' " +
            "and e.createDay BETWEEN :startDay AND :endDay ")
    ResponseRatingDto findRatingByAdmin(String email, LocalDate startDay, LocalDate endDay);
}
