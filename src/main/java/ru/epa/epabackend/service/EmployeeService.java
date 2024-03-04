package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeFullDto addEmployee(EmployeeDtoRequest employeeRtoRequest);

    EmployeeFullDto updateEmployee(Long employeeId, EmployeeDtoRequest employeeRtoRequest);

    void deleteEmployee(Long employeeId);

    List<EmployeeShortDto> getAllEmployees();

    EmployeeFullDto getEmployeeById(Long employeeId);

    UserDetailsService userDetailsService();

    Employee getEmployeeByEmail(String email);

    Employee getEmployee(Long employeeId);
}
