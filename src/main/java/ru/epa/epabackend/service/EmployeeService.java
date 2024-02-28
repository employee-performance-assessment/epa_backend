package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.employee.*;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponseFull addEmployee(EmployeeRtoRequest employeeRtoRequest);

    EmployeeDtoResponseFull updateEmployee(Long employeeId, EmployeeRtoRequest employeeRtoRequest);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoResponseShort> getAllEmployees();

    EmployeeDtoResponseFull getEmployeeById(Long employeeId);
}
