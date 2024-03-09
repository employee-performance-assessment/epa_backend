package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.project.UpdateProjectRto;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.util.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ProjectServiceImpl содержит бизнес-логику работы с проектами
 *
 * @author Константин Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Project.class)));
    }

    @Override
    public ProjectShortDto save(NewProjectRto newProjectRto, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        return projectMapper.mapToShortDto(projectRepository.save(projectMapper.mapToEntity(newProjectRto, List.of(admin))));
    }

    @Override
    public ProjectShortDto findDtoById(Long projectId, String email) {
        return projectMapper.mapToShortDto(findById(projectId));
    }

    @Override
    public ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Employee employee = employeeService.getEmployee(employeeId);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        if (project.getEmployees().contains(employee))
            throw new ConflictException(String.format("Сотрудник с id %d уже добавлен к проекту", employeeId));
        List<Employee> employees = project.getEmployees();
        employees.add(employee);
        return projectMapper.mapToProjectEmployeesDto(projectRepository.save(project),
                employees.stream().map(employeeMapper::mapToShortDto).collect(Collectors.toList()));
    }

    @Override
    public List<ProjectShortDto> findByUserEmail(String email) {
        return projectRepository.findByEmployees(employeeService.getEmployeeByEmail(email)).stream()
                .map(projectMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeShortDto> findByProjectIdAndRole(Long projectId, Role role, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        return employeeRepository
                .findByProjectsAndRole(findById(projectId), role)
                .stream().map(employeeMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public ProjectShortDto update(Long projectId, UpdateProjectRto updateProjectRto, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        if (updateProjectRto.getName() != null)
            project.setName(updateProjectRto.getName());
        if (updateProjectRto.getStatus() != null)
            project.setStatus(updateProjectRto.getStatus());
        return projectMapper.mapToShortDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long projectId, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        projectRepository.delete(project);
    }

    @Override
    public void deleteEmployeeFromProject(Long projectId, Long employeeId, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        Employee employee = employeeService.getEmployee(employeeId);
        checkUserAndProject(employee, project);
        project.getEmployees().remove(employee);
        projectRepository.save(project);
    }

    @Override
    public void checkUserAndProject(Employee user, Project project) {
        if (!user.getProjects().contains(project))
            throw new ConflictException(String.format("%s с email %s не относится к проекту с id %d",
                    user.getRole(), user.getEmail(), project.getId()));
    }
}
