package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.employee.*;
import ru.epa.epabackend.model.Employee;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    ResponseEmployeeFullDto mapToFullDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(RequestEmployeeDto requestEmployeeDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(RequestEmployeeShortDto requestEmployeeShortDto);

    ResponseEmployeeShortDto mapToShortDto(Employee employee);

    ResponseEmployeeAnalyticsDto mapToAnalyticsDto(Employee employee);

    default List<ResponseEmployeeShortDto> mapList(List<Employee> employees) {
        return employees.stream().map(this::mapToShortDto).collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "password", ignore = true)
    Employee updateFields(RequestEmployeeDto requestEmployeeDto, @MappingTarget Employee oldEmployee);
}
