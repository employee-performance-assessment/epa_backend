package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.util.List;

public interface EmployeeEvaluationRepository extends JpaRepository<EmployeeEvaluation, Long> {
    List<EmployeeEvaluation> findAllByAppraiserId(Long appraiserId);
}

