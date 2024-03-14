package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortRequestDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(EmployeeRequestDto employeeRtoRequest);

    Employee createSelfRegister(EmployeeShortRequestDto employeeShortRequestDto);

    Employee update(Long employeeId, EmployeeRequestDto employeeRtoRequest);

    void delete(Long employeeId);

    List<Employee> findAll();

    Employee findByIdDto(Long employeeId);

    UserDetailsService userDetailsService();

    Employee findByEmail(String email);

    Employee findById(Long employeeId);
}
