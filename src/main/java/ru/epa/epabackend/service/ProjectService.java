package ru.epa.epabackend.service;


import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface ProjectService {
    Project findById(Long projectId);

    Project create(ProjectCreateRequestDto projectCreateRequestDto, String email);

    Project findDtoById(Long projectId, String email);

    Project saveWithEmployee(Long projectId, Long employeeId, String email);

    List<Project> findAllByCreator(String email);

    List<Employee> findAllByProjectIdAndRole(Long projectId, Role role, String email);

    Project update(Long projectId, ProjectUpdateRequestDto projectUpdateRequestDto, String email);

    void delete(Long projectId, String email);

    void deleteEmployeeFromProject(Long projectId, Long employeeId, String email);

    void checkUserAndProject(Employee user, Project project);
}
