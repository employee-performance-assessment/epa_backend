package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByEmployees(Employee employee);

    Optional<Project> findByName(String name);

    boolean existsByNameAndEmployeesEmailIn(String name, List<String> emails);
}
