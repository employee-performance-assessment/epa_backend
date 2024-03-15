package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.util.Role;

import java.util.List;

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
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Проект с id %s не найден", projectId)));
    }

    @Override
    public Project create(
            ProjectCreateRequestDto newProjectRto, String email) {
        Employee admin = employeeService.findByEmail(email);
        return projectRepository
                .save(projectMapper.mapToEntity(newProjectRto, List.of(admin)));
    }

    @Override
    public Project findDtoById(Long projectId, String email) {
        return findById(projectId);
    }

    @Override
    public Project saveWithEmployee(Long projectId, Long employeeId, String email) {
        Employee admin = employeeService.findByEmail(email);
        Employee employee = employeeService.findById(employeeId);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        if (project.getEmployees().contains(employee))
            throw new ConflictException(String.format("Сотрудник с id %d уже добавлен к проекту", employeeId));
        List<Employee> employees = project.getEmployees();
        employees.add(employee);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findAllByUserEmail(String email) {
        return projectRepository.findByEmployees(employeeService.findByEmail(email));
    }

    @Override
    public List<Employee> findAllByProjectIdAndRole(Long projectId, Role role, String email) {
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        return employeeRepository.findByProjectsAndRole(findById(projectId), role);
    }

    @Override
    public Project update(
            Long projectId, ProjectUpdateRequestDto updateProjectRto, String email) {
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        projectMapper.updateFields(updateProjectRto, project);
        return projectRepository.save(project);
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
