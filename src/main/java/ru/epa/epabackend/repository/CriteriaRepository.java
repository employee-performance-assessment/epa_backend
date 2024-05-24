package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Criteria;

import java.util.List;
import java.util.Optional;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
    boolean existsByName(String name);

    Optional<Criteria> findByName(String name);

    List<Criteria> findAllByIsDefault(boolean isDefault);
}
