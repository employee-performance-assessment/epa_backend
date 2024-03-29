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
 * Класс TaskServiceImpl содержит методы действий с задачами.
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
    public List<Task> findAll(String email) {
        return taskRepository.findAllByOwnerEmail(email);
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public Task findDtoById(Long taskId, String email) {
        Task task = findById(taskId);
        Project project = task.getProject();
        Employee admin = employeeService.findByEmail(email);
        projectService.checkUserAndProject(admin, project);
        return task;
    }

    /**
     * Создание задачи админом
     */
    @Override
    public Task create(TaskRequestDto taskRequestDto, String email) {
        Project project = projectService.findById(taskRequestDto.getProjectId());
        Employee executor = employeeService.findById(taskRequestDto.getExecutorId());
        Employee admin = employeeService.findByEmail(email);
        projectService.checkUserAndProject(admin, project);
        taskRequestDto.setStatus("NEW");
        return taskRepository.save(taskMapper.mapToEntity(taskRequestDto, project, executor, admin));
    }

    /**
     * Обновление задачи админом
     */
    @Override
    public Task update(Long taskId, TaskRequestDto taskRequestDto, String email) {
        Task oldTask = findById(taskId);
        Project project = oldTask.getProject();
        Employee admin = employeeService.findByEmail(email);
        projectService.checkUserAndProject(admin, project);
        Employee executor = checkExecutor(taskRequestDto, oldTask);
        taskMapper.updateFields(taskRequestDto, project, executor, oldTask);
        if (oldTask.getStatus() == TaskStatus.DONE) {
            setPointsToEmployeeAfterTaskDone(oldTask);
            oldTask.setFinishDate(LocalDate.now());
        }
        return taskRepository.save(oldTask);
    }

    /**
     * Удаление задачи админом
     */
    @Override
    public void delete(Long taskId, String email) {
        Task task = findById(taskId);
        Project project = task.getProject();
        Employee admin = employeeService.findByEmail(email);
        projectService.checkUserAndProject(admin, project);
        taskRepository.delete(task);
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

    /**
     * Проставление очков после того как задача выполнена
     */
    private void setPointsToEmployeeAfterTaskDone(Task task) {
        Period period = Period.between(LocalDate.now(), task.getDeadLine());
        Integer days = period.getDays();
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    /**
     * Получение корректного статуса задачи
     */
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

    /**
     * Проверка исполнителя задач при обновлении задачи. Если исполнитель поменялся, то
     * ищем его айди в репозитории, если находим, то возвращаем его. Если не найден, то
     * берем из задачи старого исполнителя.
     */
    private Employee checkExecutor(TaskRequestDto taskRequestDto, Task oldTask) {
        Employee executor;
        if (taskRequestDto.getExecutorId() != null) {
            executor = employeeService.findById(taskRequestDto.getExecutorId());
        } else {
            executor = oldTask.getExecutor();
        }
        return executor;
    }
}