package ru.epa.epabackend.service;


import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.project.UpdateProjectRto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface ProjectService {
    Project findById(Long projectId);

    ProjectShortDto save(NewProjectRto newProjectRto, String email);

    ProjectShortDto findDtoById(Long projectId, String email);

    ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId, String email);

    List<ProjectShortDto> findByAdminEmail(String email);

    List<EmployeeShortDto> findByProjectIdAndRole(Long projectId, Role role, String email);

    ProjectShortDto update(Long projectId, UpdateProjectRto updateProjectRto, String email);

    void delete(Long projectId, String email);

    void deleteEmployeeFromProject(Long projectId, Long employeeId, String email);

    void checkUserAndProject(Employee user, Project project);
}
