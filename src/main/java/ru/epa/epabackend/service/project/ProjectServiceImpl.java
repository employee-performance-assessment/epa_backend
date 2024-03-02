package ru.epa.epabackend.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeForListDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.project.UpdateProjectRto;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.Role;

import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.exception.ExceptionDescriptions.PROJECT_NOT_FOUND;

/**
 * Класс ProjectServiceImpl содержит бизнес-логику работы с проектами
 *
 * @author Константин Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
    }

    @Override
    public ProjectShortDto save(NewProjectRto newProjectRto, Long adminId) {
        Employee admin = employeeService.getEmployee(adminId);
        return projectMapper.toProjectShortDto(projectRepository.save(projectMapper.toProject(newProjectRto, admin)));
    }

    @Override
    public ProjectShortDto findDtoById(Long projectId) {
        return projectMapper.toProjectShortDto(findById(projectId));
    }

    @Override
    public ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId) {
        Project project = findById(projectId).setId(projectId);
        return projectMapper.toProjectEmployeesDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectShortDto> findByAdminId(Long adminId) {
        return projectRepository.findByEmployees(employeeService.getEmployee(adminId)).stream()
                .map(projectMapper::toProjectShortDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeForListDto> findByProjectIdAndRole(Long projectId, Role role) {
        return employeeRepository
                .findByProjectsAndRole(findById(projectId), role)
                .stream().map(EmployeeMapper::toEmployeeForListDto).collect(Collectors.toList());
    }

    @Override
    public ProjectShortDto update(Long projectId, UpdateProjectRto updateProjectRto) {
        Project project = findById(projectId);
        if (updateProjectRto.getName() != null)
            project.setName(updateProjectRto.getName());
        if (updateProjectRto.getStatus() != null)
            project.setStatus(updateProjectRto.getStatus());
        return projectMapper.toProjectShortDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long projectId) {
        projectRepository.delete(findById(projectId));
    }

    @Override
    public void deleteEmployeeFromProject(Long projectId, Long employeeId) {
        Project project = findById(projectId);
        Employee employee = employeeService.getEmployee(employeeId);
        if(project.getEmployees().contains(employee))
            project.getEmployees().remove(employee);
        projectRepository.save(project);
    }
}