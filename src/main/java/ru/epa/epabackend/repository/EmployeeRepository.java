package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.dto.employee.EmployeeForListDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Role;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLogin(String login);

    List<Employee> findByProjectsAndRole(Project project, Role role);
}
