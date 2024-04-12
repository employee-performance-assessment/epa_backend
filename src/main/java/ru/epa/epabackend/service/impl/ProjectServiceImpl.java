package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.project.RequestProjectCreateDto;
import ru.epa.epabackend.dto.project.RequestProjectUpdateDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.util.Role;

import java.util.List;

/**
 * Класс ProjectServiceImpl содержит бизнес-логику работы с проектами
 *
 * @author Константин Осипов
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    /**
     * Получение проекта по id
     */
    @Override
    @Transactional(readOnly = true)
    public Project findById(Long projectId) {
        log.info("Получение проекта по id {}", projectId);
        return projectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Проект с id %s не найден", projectId)));
    }

    /**
     * Создание нового проекта
     */
    @Override
    public Project create(RequestProjectCreateDto requestProjectCreateDto, String email) {
        log.info("Создание нового проекта {}", requestProjectCreateDto.getName());
        Employee admin = employeeService.findByEmail(email);
        return projectRepository
                .save(projectMapper.mapToEntity(requestProjectCreateDto, List.of(admin)));
    }

    /**
     * Получение dto проекта по id
     */
    @Override
    public Project findDtoById(Long projectId, String email) {
        log.info("Получение dto проекта по идентификатору {}", projectId);
        return findById(projectId);
    }

    /**
     * Сохранение сотрудника в проект
     */
    @Override
    public Project saveWithEmployee(Long projectId, Long employeeId, String email) {
        log.info("Сохранение сотрудника с идентификатором {} в проект с идентификатором {}", employeeId, projectId);
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

    /**
     * Поиск всех создателей
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllByCreator(String email) {
        log.info("Поиск всех создателей");
        Employee employee = employeeService.findByEmail(email);
        if (employee.getRole() == Role.ROLE_ADMIN) {
            return projectRepository.findByEmployees(employee);
        }
        return projectRepository.findByEmployees(employee.getCreator());
    }

    /**
     * Поиск всех по проекту и роли
     */
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllByProjectIdAndRole(Long projectId, Role role, String email) {
        log.info("Поиск всех по проекту с идентификатором {} и роли {}", projectId, role);
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        return employeeRepository.findByProjectsAndRole(findById(projectId), role);
    }

    /**
     * Обновление проекта
     */
    @Override
    public Project update(Long projectId, RequestProjectUpdateDto requestProjectUpdateDto, String email) {
        log.info("Обновление проекта с идентификатором {}", projectId);
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        projectMapper.updateFields(requestProjectUpdateDto, project);
        return projectRepository.save(project);
    }

    /**
     * Удаление проекта
     */
    @Override
    public void delete(Long projectId, String email) {
        log.info("Удаление проекта с идентификатором {}", projectId);
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        if(taskRepository.existsByProjectId(projectId)) {
            throw new ConflictException("Невозможно удалить проект, пока к нему привязаны задачи");
        }
        projectRepository.delete(project);
    }

    /**
     * Удаление сотрудника из проекта
     */
    @Override
    public void deleteEmployeeFromProject(Long projectId, Long employeeId, String email) {
        log.info("Удаление сотрудника с идентификатором {} из проекта с идентификатором {}", employeeId, projectId);
        Employee admin = employeeService.findByEmail(email);
        Project project = findById(projectId);
        checkUserAndProject(admin, project);
        Employee employee = employeeService.findById(employeeId);
        checkUserAndProject(employee, project);
        project.getEmployees().remove(employee);
        projectRepository.save(project);
    }

    /**
     * Проверка сотрудника и проекта
     */
    @Override
    public void checkUserAndProject(Employee user, Project project) {
        log.info("Проверка сотрудника {} и проекта {}", user, project);
        if (!user.getProjects().contains(project))
            throw new BadRequestException(String.format("%s с email %s не относится к проекту с id %d",
                    user.getRole(), user.getEmail(), project.getId()));
    }
}