package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Technology;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
}
