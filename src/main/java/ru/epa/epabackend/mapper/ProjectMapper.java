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
        return new ProjectShortDto(project.getId(), project.getName(), project.getStatus());
    }

    public Project toProject(NewProjectRto newProjectRto, Employee admin) {
        return new Project()
                .setName(newProjectRto.getName())
                .setStatus(ProjectStatus.TODO)
                .setEmployees(List.of(admin));
    }

    public Project toProject(ProjectShortDto projectShortDto) {
        return new Project()
                .setStatus(projectShortDto.getStatus())
                .setName(projectShortDto.getName())
                .setStatus(projectShortDto.getStatus());
    }

    public ProjectEmployeesDto toProjectEmployeesDto(Project project) {
        return new ProjectEmployeesDto(project.getId(), project.getName(), project.getEmployees());
    }
}