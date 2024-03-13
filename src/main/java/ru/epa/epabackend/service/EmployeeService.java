package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeFullResponseDto create(EmployeeRequestDto employeeRtoRequest);

    EmployeeFullResponseDto createSelfRegister(EmployeeRequestDto employeeRtoRequest);

    EmployeeFullResponseDto update(Long employeeId, EmployeeRequestDto employeeRtoRequest);

    void delete(Long employeeId);

    List<EmployeeShortResponseDto> findAll();

    EmployeeFullResponseDto findByIdDto(Long employeeId);

    UserDetailsService userDetailsService();

    Employee findByEmail(String email);

    Employee findById(Long employeeId);
}
