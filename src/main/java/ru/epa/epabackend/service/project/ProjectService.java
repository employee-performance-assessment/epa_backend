package ru.epa.epabackend.service.project;


import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;

import java.util.List;

public interface ProjectService {
    ProjectShortDto save(NewProjectRto newProjectRto);

    ProjectShortDto findDtoById(Long id);

    ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId);

    List<ProjectShortDto> findByAdminId(Long adminId);
}
