package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeFindAllResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateFindByIdFindAllUpdateResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
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

    ProjectCreateFindByIdFindAllUpdateResponseDto mapToShortDto(Project project);

    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "created", expression = "java(java.time.LocalDate.now())")
    Project mapToEntity(ProjectCreateRequestDto newProjectRto, List<Employee> employees);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project mapToEntity(ProjectCreateFindByIdFindAllUpdateResponseDto projectShortDto);

    ProjectSaveWithEmployeeResponseDto mapToProjectEmployeesDto(Project project, List<EmployeeFindAllResponseDto> employeeShortDtoList);
}
