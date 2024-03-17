package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

