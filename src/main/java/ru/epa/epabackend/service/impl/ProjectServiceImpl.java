package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
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
        return projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Проект с id %s не найден", projectId)));
    }

    @Override
    public ProjectShortResponseDto create(
            ProjectCreateRequestDto newProjectRto, String email) {
        Employee admin = employeeService.findByEmail(email);
        return projectMapper.mapToShortDto(projectRepository
                .save(projectMapper.mapToEntity(newProjectRto, List.of(admin))));
    }

    @Override
    public ProjectShortResponseDto findDtoById(Long projectId, String email) {
        return projectMapper.mapToShortDto(findById(projectId));
    }

    @Override
    public ProjectSaveWithEmployeeResponseDto saveWithEmployee(Long projectId, Long employeeId, String email) {
        Employee admin = employeeService.findByEmail(email);
        Employee employee = employeeService.findById(employeeId);
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
    public List<ProjectShortResponseDto> findAllByUserEmail(String email) {
        return projectRepository.findByEmployees(employeeService.findByEmail(email)).stream()
                .map(projectMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeShortResponseDto> findAllByProjectIdAndRole(Long projectId, Role role, String email) {
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        return employeeRepository
                .findByProjectsAndRole(findById(projectId), role)
                .stream().map(employeeMapper::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public ProjectShortResponseDto update(
            Long projectId, ProjectUpdateRequestDto updateProjectRto, String email) {
        Employee admin = employeeService.findByEmail(email);
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
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        projectRepository.delete(project);
    }

    @Override
    public void deleteEmployeeFromProject(Long projectId, Long employeeId, String email) {
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        Employee employee = employeeService.findById(employeeId);
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
