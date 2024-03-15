package ru.epa.epabackend.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.service.TaskService;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Класс TaskServiceImpl содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    /**
     * Получение списка всех задач админом
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public Task findDtoById(Long taskId) {
        return findById(taskId);
    }

    /**
     * Создание задачи админом
     */
    @Override
    public Task create(TaskRequestDto taskCreateUpdateRequestDto) {
        Project project = projectService.findById(taskCreateUpdateRequestDto.getProjectId());
        Employee executor = employeeService.findById(taskCreateUpdateRequestDto.getExecutorId());
        taskCreateUpdateRequestDto.setStatus("NEW");
        checkProjectContainsExecutor(project, executor);
        return taskRepository.save(taskMapper.mapToEntity(taskCreateUpdateRequestDto, project, executor));

    }

    /**
     * Обновление задачи админом
     */
    @Override
    public Task update(
            Long taskId, TaskRequestDto taskCreateUpdateRequestDto) {
        Task oldTask = findById(taskId);
        Project project = projectService.findById(taskCreateUpdateRequestDto.getProjectId());
        Employee executor = checkExecutor(taskCreateUpdateRequestDto, oldTask);

        taskMapper.updateFields(taskCreateUpdateRequestDto, project, executor, oldTask);
        if (oldTask.getStatus() == TaskStatus.DONE) {
            setPointsToEmployeeAfterTaskDone(taskCreateUpdateRequestDto, oldTask);
            oldTask.setFinishDate(LocalDate.now());
        }
        return taskRepository.save(oldTask);
    }

    /**
     * Удаление задачи админом
     */
    @Override
    public void delete(Long taskId) {
        taskRepository.delete(findById(taskId));
    }

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    @Override
    public List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findAllByProjectIdAndStatus(projectId, status);
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByExecutorIdFilters(String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
        try {
            return taskRepository.findAllByExecutorIdFilters(employee.getId(), getTaskStatus(status));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Неверный статус: " + status);
        }
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public Task findByIdAndExecutorId(Principal principal, Long taskId) {
        Employee employee = employeeService.findByEmail(principal.getName());
        return findByIdAndExecutorId(taskId, employee.getId());
    }

    /**
     * Обновление статуса задачи
     */
    @Override
    public Task updateStatus(Long taskId, String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
        try {
            TaskStatus taskStatus = getTaskStatus(status);
            Task task = findByIdAndExecutorId(taskId, employee.getId());
            if (taskStatus == TaskStatus.IN_PROGRESS) {
                task.setStartDate(LocalDate.now());
            }
            task.setStatus(taskStatus);
            return taskRepository.save(task);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Неверный статус: " + status);
        }
    }

    /**
     * Получение задачи из репозитория по ID задачи и ID исполнителя
     */
    private Task findByIdAndExecutorId(Long taskId, Long employeeId) {
        return taskRepository.findByIdAndExecutorId(taskId, employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Задача с id %s и исполнителем с id %s не найдена",
                        taskId, employeeId)));
    }

    private void setPointsToEmployeeAfterTaskDone(TaskRequestDto dto, Task task) {
        Period period = Period.between(LocalDate.now(), dto.getDeadLine());
        Integer days = period.getDays();
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    private void checkProjectContainsExecutor(Project project, Employee employee) {
        if (!project.getEmployees().contains(employee)) {
            throw new EntityNotFoundException(String.format("Пользователя с id %d нет в проекте с id %d",
                    employee.getId(), project.getId()));
        }
    }

    private TaskStatus getTaskStatus(String status) {
        TaskStatus taskStatus = null;
        if (status != null) {
            taskStatus = EnumUtils.getEnum(TaskStatus.class, status);
        }
        return taskStatus;
    }

    /**
     * Получение задачи из репозитория по ID
     */
    private Task findById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Задача с id %s не найдена", taskId)));
    }

    private Employee checkExecutor(TaskRequestDto taskCreateUpdateRequestDto, Task oldTask) {
        Employee executor;
        if (taskCreateUpdateRequestDto.getExecutorId() != null) {
            executor = employeeService.findById(taskCreateUpdateRequestDto.getExecutorId());
        } else {
            executor = oldTask.getExecutor();
        }
        return executor;
    }
}