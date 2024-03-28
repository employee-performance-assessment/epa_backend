package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Criteria;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
}
