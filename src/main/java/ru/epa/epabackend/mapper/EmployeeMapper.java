package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDtoResponseFull mapToFullDto(Employee employee);

    Employee mapToEntity(EmployeeRtoRequest employeeRtoRequest);

    EmployeeDtoResponseShort mapToShortDto(Employee employee);
}

