package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
