package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.employee.EmployeeDtoFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoNew;
import ru.epa.epabackend.dto.employee.EmployeeDtoShort;
import ru.epa.epabackend.dto.employee.EmployeeDtoUpdate;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoFull addEmployee(EmployeeDtoNew employeeDtoNew);

    EmployeeDtoFull updateEmployee(Long employeeId, EmployeeDtoUpdate employeeDtoUpdate);

    void deleteEmployee(Long employeeId);

    List<EmployeeDtoShort> getAllEmployees();

    EmployeeDtoFull getEmployeeById(Long employeeId);
}
