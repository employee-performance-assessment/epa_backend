package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponseFull addEmployee(EmployeeDtoRequest employeeDtoRequest);

    EmployeeDtoResponseFull updateEmployee(Long employeeId, EmployeeDtoRequest employeeDtoRequest);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoResponseShort> getAllEmployees();

    EmployeeDtoResponseFull getEmployeeById(Long employeeId);

    UserDetailsService userDetailsService();

    Employee getEmployeeByLogin(String login);

    Employee getEmployee(Long employeeId);
}
