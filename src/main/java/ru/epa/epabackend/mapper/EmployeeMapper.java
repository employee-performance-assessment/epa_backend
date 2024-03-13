package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeFullResponseDto mapToFullDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(EmployeeRequestDto employeeRtoRequest);

    EmployeeShortResponseDto mapToShortDto(Employee employee);
}
