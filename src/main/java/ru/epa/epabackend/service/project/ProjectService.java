package ru.epa.epabackend.service.project;


import ru.epa.epabackend.dto.employee.EmployeeForListDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.project.UpdateProjectRto;
import ru.epa.epabackend.util.Role;

import java.util.List;

public interface ProjectService {
    ProjectShortDto save(NewProjectRto newProjectRto, Long adminId);

    ProjectShortDto findDtoById(Long projectId);

    ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId);

    List<ProjectShortDto> findByAdminId(Long adminId);

    List<EmployeeForListDto> findByProjectIdAndRole(Long projectId, Role role);

    ProjectShortDto update(Long projectId, UpdateProjectRto updateProjectRto);

    void delete(Long projectId);

    void deleteEmployeeFromProject(Long projectId, Long employeeId);
}
