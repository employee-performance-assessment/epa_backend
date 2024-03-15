package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ProjectMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов и Константин Осипов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectMapper {

    ProjectShortResponseDto mapToShortDto(Project project);

    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "created", expression = "java(java.time.LocalDate.now())")
    Project mapToEntity(ProjectCreateRequestDto newProjectRto, List<Employee> employees);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project mapToEntity(ProjectShortResponseDto projectShortDto);

    ProjectSaveWithEmployeeResponseDto mapToProjectEmployeesDto(Project project, List<EmployeeShortResponseDto> employeeShortDtoList);

    default List<ProjectShortResponseDto> mapAsList(List<Project> projects) {
        return projects.stream().map(this::mapToShortDto).collect(Collectors.toList());
    }
  
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project updateFields(ProjectUpdateRequestDto updateProjectRto, @MappingTarget Project oldProject);
}
