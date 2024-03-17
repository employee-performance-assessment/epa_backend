package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.EmployeeEvaluation;

public interface EmployeeEvaluationRepository extends JpaRepository<EmployeeEvaluation, Long> {
}

