package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponseFull addEmployee(EmployeeRtoRequest employeeRtoRequest);

    EmployeeDtoResponseFull updateEmployee(Long employeeId, EmployeeRtoRequest employeeRtoRequest);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoResponseShort> getAllEmployees();

    EmployeeDtoResponseFull getEmployeeById(Long employeeId);

    UserDetailsService userDetailsService();

    Employee getEmployeeByLogin(String login);

    List<EmployeeForListDto> findByProjectIdAndRole(Long projectId, Role role);

    Employee getEmployee(Long employeeId);
}
