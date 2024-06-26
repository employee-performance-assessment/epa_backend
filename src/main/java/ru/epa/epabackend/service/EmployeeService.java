package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.RequestEmployeeDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(RequestEmployeeDto requestEmployeeDto, String email);

    Employee createSelfRegister(RequestEmployeeShortDto requestEmployeeShortDto);

    Employee update(Long employeeId, RequestEmployeeDto requestEmployeeDto, String adminEmail);

    void delete(Long employeeId, String email);

    List<Employee> findAll(String email);

    UserDetailsService userDetailsService();

    Employee findByEmail(String email);

    Employee findById(Long employeeId);

    List<Employee> findAllByCreatorEmail(String email);

    List<Integer> findAllYearsFromAdminCreation(String email);

    void checkAdminForEmployee(Employee admin, Employee employee);

    void checkEvaluatorForEmployee(Employee evaluator, Employee evaluated);

    Employee findByIdDto(Long employeeId, String email);
}
