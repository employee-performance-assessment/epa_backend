package ru.epa.epabackend.service;


import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface ProjectService {
    Project findById(Long projectId);

    ProjectShortResponseDto create(ProjectCreateRequestDto newProjectRto, String email);

    ProjectShortResponseDto findDtoById(Long projectId, String email);

    ProjectSaveWithEmployeeResponseDto saveWithEmployee(Long projectId, Long employeeId, String email);

    List<ProjectShortResponseDto> findAllByUserEmail(String email);

    List<EmployeeShortResponseDto> findAllByProjectIdAndRole(Long projectId, Role role, String email);

    ProjectShortResponseDto update(Long projectId, ProjectUpdateRequestDto updateProjectRto, String email);

    void delete(Long projectId, String email);

    void deleteEmployeeFromProject(Long projectId, Long employeeId, String email);

    void checkUserAndProject(Employee user, Project project);
}
