package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.RequestEmployeeDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(RequestEmployeeDto requestEmployeeDto, String email);

    Employee createSelfRegister(RequestEmployeeShortDto requestEmployeeShortDto);

    Employee update(Long employeeId, RequestEmployeeDto requestEmployeeDto);

    void delete(Long employeeId);

    List<Employee> findAll();

    Employee findByIdDto(Long employeeId);

    UserDetailsService userDetailsService();

    Employee findByEmail(String email);

    Employee findById(Long employeeId);

    List<Employee> findAllByCreatorEmail(String email);

    void checkAdminForEmployee(Employee admin, Employee employee);
}
