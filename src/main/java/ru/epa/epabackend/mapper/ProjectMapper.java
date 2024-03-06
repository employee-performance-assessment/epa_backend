package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;

import java.util.List;

/**
 * Класс ProjectMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов и Константин Осипов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectMapper {

    ProjectShortDto mapToShortDto(Project project);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Project mapToEntity(NewProjectRto newProjectRto, Employee admin);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project mapToEntity(ProjectShortDto projectShortDto);

    ProjectEmployeesDto mapToProjectEmployeesDto(Project project, List<EmployeeShortDto> employeeShortDtoList);
}
