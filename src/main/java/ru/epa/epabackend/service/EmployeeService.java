package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.*;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponseFull addEmployee(EmployeeRtoRequest employeeRtoRequest);

    EmployeeDtoResponseFull updateEmployee(Long employeeId, EmployeeRtoRequest employeeRtoRequest);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoResponseShort> getAllEmployees();

    EmployeeDtoResponseFull getEmployeeById(Long employeeId);

    UserDetailsService userDetailsService();

    Employee getEmployeeByEmail(String email);

    Employee getEmployee(Long employeeId);
}
