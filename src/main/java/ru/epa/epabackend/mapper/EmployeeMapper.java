package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.model.Employee;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeFullResponseDto mapToFullDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(EmployeeRequestDto employeeRtoRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(EmployeeShortRequestDto employeeShortRequestDto);

    EmployeeShortResponseDto mapToShortDto(Employee employee);

    default List<EmployeeShortResponseDto> mapList(List<Employee> employees) {
        return employees.stream().map(this::mapToShortDto).collect(Collectors.toList());
    }
  
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "password", ignore = true)
    Employee updateFields(EmployeeRequestDto employeeDtoRequest,  @MappingTarget Employee oldEmployee);
}
