package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.ProjectStatus;

import java.util.List;

/**
 * Класс ProjectMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов и Константин Осипов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectMapper {

    ProjectShortDto mapToShortDto(Project project);

    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "created", expression = "java(java.time.LocalDate.now())")
    Project mapToEntity(NewProjectRto newProjectRto, List<Employee> employees);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project mapToEntity(ProjectShortDto projectShortDto);

    ProjectEmployeesDto mapToProjectEmployeesDto(Project project, List<EmployeeShortDto> employeeShortDtoList);
}
