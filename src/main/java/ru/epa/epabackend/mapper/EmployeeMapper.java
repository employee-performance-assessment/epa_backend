package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeFullDto mapToFullDto(Employee employee);

    Employee mapToEntity(EmployeeDtoRequest employeeRtoRequest);

    EmployeeShortDto mapToShortDto(Employee employee);
}
