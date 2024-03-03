package ru.epa.epabackend.mapper;

import org.springframework.stereotype.Component;
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
@Component
public class ProjectMapper {
    public ProjectShortDto toProjectShortDto(Project project) {
        return ProjectShortDto.builder()
                .id(project.getId())
                .name(project.getName())
                .status(project.getStatus())
                .build();
    }

    public Project toProject(NewProjectRto newProjectRto, Employee admin) {
        return Project.builder()
                .name(newProjectRto.getName())
                .status(ProjectStatus.TODO)
                .employees(List.of(admin))
                .build();
    }

    public Project toProject(ProjectShortDto projectShortDto) {
        return Project.builder()
                .status(projectShortDto.getStatus())
                .name(projectShortDto.getName())
                .status(projectShortDto.getStatus())
                .build();
    }

    public ProjectEmployeesDto toProjectEmployeesDto(Project project) {
        return ProjectEmployeesDto
                .builder()
                .id(project.getId())
                .name(project.getName())
                .employees(project.getEmployees())
                .build();
    }
}