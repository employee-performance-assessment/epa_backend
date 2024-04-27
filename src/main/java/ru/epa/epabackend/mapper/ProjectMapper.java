package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.project.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;

import java.util.List;

/**
 * Класс ProjectMapper содержит преобразование сущности
 *
 * @author Владислав Осипов и Константин Осипов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectMapper {

    ResponseProjectShortDto mapToShortDto(Project project);

    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "created", expression = "java(java.time.LocalDate.now())")
    Project mapToEntity(RequestProjectCreateDto requestProjectCreateDto, List<Employee> employees);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project mapToEntity(ResponseProjectShortDto responseProjectShortDto);

    ResponseProjectSaveWithEmployeeDto mapToProjectEmployeesDto(Project project, List<ResponseEmployeeShortDto> employeeShortDtoList);

    List<ResponseProjectShortDto> mapAsList(List<Project> projects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project updateFields(RequestProjectUpdateDto requestProjectUpdateDto, @MappingTarget Project oldProject);

    Project mapToEntity(RequestProjectCreateDto requestProjectCreateDto);

    Project mapToEntity(RequestProjectCreateUpdateDto requestProjectCreateUpdateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "id", ignore = true)
    Project updateFields(RequestProjectCreateUpdateDto requestProjectCreateUpdateDto, @MappingTarget Project project);

    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "created", expression = "java(java.time.LocalDate.now())")
    Project mapToEntity(RequestProjectCreateUpdateDto requestProjectCreateUpdateDto, List<Employee> employees);
}