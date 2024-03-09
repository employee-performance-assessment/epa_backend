package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeFullDto mapToFullDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(EmployeeDtoRequest employeeRtoRequest);

    EmployeeShortDto mapToShortDto(Employee employee);
}
