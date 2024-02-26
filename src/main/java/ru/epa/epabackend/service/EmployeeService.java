package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.EmployeeDto;
import ru.epa.epabackend.dto.EmployeeDtoShort;

import java.util.List;

public interface EmployeeService {
    EmployeeDto addEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoShort> getAllEmployees();

    EmployeeDto getEmployeeById(Long employeeId);
}
