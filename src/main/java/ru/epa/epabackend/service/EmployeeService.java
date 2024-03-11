package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.epa.epabackend.dto.employee.EmployeeCreateUpdateFindByIdResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeCreateUpdateRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeFindAllResponseDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeCreateUpdateFindByIdResponseDto create(EmployeeCreateUpdateRequestDto employeeRtoRequest);

    EmployeeCreateUpdateFindByIdResponseDto createSelfRegister(EmployeeCreateUpdateRequestDto employeeRtoRequest);

    EmployeeCreateUpdateFindByIdResponseDto update(Long employeeId, EmployeeCreateUpdateRequestDto employeeRtoRequest);

    void delete(Long employeeId);

    List<EmployeeFindAllResponseDto> findAll();

    EmployeeCreateUpdateFindByIdResponseDto findByIdDto(Long employeeId);

    UserDetailsService userDetailsService();

    Employee findByEmail(String email);

    Employee findById(Long employeeId);
}
