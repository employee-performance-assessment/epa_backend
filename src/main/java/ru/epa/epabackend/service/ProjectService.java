package ru.epa.epabackend.service;


import ru.epa.epabackend.dto.employee.EmployeeFindAllResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateFindByIdFindAllUpdateResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface ProjectService {
    Project findById(Long projectId);

    ProjectCreateFindByIdFindAllUpdateResponseDto create(ProjectCreateRequestDto newProjectRto, String email);

    ProjectCreateFindByIdFindAllUpdateResponseDto findDtoById(Long projectId, String email);

    ProjectSaveWithEmployeeResponseDto saveWithEmployee(Long projectId, Long employeeId, String email);

    List<ProjectCreateFindByIdFindAllUpdateResponseDto> findAllByUserEmail(String email);

    List<EmployeeFindAllResponseDto> findAllByProjectIdAndRole(Long projectId, Role role, String email);

    ProjectCreateFindByIdFindAllUpdateResponseDto update(Long projectId, ProjectUpdateRequestDto updateProjectRto, String email);

    void delete(Long projectId, String email);

    void deleteEmployeeFromProject(Long projectId, Long employeeId, String email);

    void checkUserAndProject(Employee user, Project project);
}
