package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
