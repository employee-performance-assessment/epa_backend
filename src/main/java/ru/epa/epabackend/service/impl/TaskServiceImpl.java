package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
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
    public List<TaskShortDto> findAll() {
        return taskRepository.findAll().stream().map(taskMapper::mapToShortDto)
                .toList();
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findDtoById(Long taskId) {
        return taskMapper.mapToFullDto(findById(taskId));
    }

    /**
     * Создание задачи админом
     */
    @Override
    public TaskFullDto create(TaskInDto taskInDto) {
        Project project = projectService.findById(taskInDto.getProjectId());
        Employee executor = employeeService.getEmployee(taskInDto.getExecutorId());
        taskInDto.setStatus("NEW");
        if (checkProjectContainsExecutor(project, executor)) {
            Task task = taskRepository.save(taskMapper.mapToEntity(taskInDto, project, executor));
            return taskMapper.mapToFullDto(task);
        } else {
            throw new BadRequestException(String.format("Сотрудника с id %d нет в проекте.",
                    taskInDto.getExecutorId()));
        }
    }

    /**
     * Обновление задачи админом
     */
    @Override
    public TaskFullDto update(Long taskId, TaskInDto taskInDto) {
        Task task = findById(taskId);
        setNotNullParamToEntity(taskInDto, task);
        if (task.getStatus() == TaskStatus.DONE) {
            setPointsToEmployeeAfterTaskDone(taskInDto, task);
            task.setFinishDate(LocalDate.now());
        }
        return taskMapper.mapToFullDto(taskRepository.save(task));
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
    public List<TaskShortDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findAllByProjectIdAndStatus(projectId, status)
                .stream().map(taskMapper::mapToShortDto).toList();
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByExecutorIdFilters(String status, Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        try {
            return taskRepository.findAllByExecutorIdFilters(employee.getId(), getTaskStatus(status)).stream()
                    .map(taskMapper::mapToShortDto).toList();
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Неверный статус: " + status);
        }
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findByIdAndExecutorId(Principal principal, Long taskId) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        return taskMapper.mapToFullDto(findByIdAndExecutorId(taskId, employee.getId()));
    }

    /**
     * Обновление статуса задачи
     */
    @Override
    public TaskFullDto updateStatus(Long taskId, String status, Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        try {
            TaskStatus taskStatus = getTaskStatus(status);
            Task task = findByIdAndExecutorId(taskId, employee.getId());
            if (taskStatus == TaskStatus.IN_PROGRESS) {
                task.setStartDate(LocalDate.now());
            }
            task.setStatus(taskStatus);
            return taskMapper.mapToFullDto(taskRepository.save(task));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Неверный статус: " + status);
        }
    }

    /**
     * Получение задачи из репозитория по ID задачи и ID исполнителя
     */
    private Task findByIdAndExecutorId(Long taskId, Long employeeId) {
        return taskRepository.findByIdAndExecutorId(taskId, employeeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Task.class)));
    }

    private void setPointsToEmployeeAfterTaskDone(TaskInDto taskInDto, Task task) {
        Period period = Period.between(LocalDate.now(), taskInDto.getDeadLine());
        Integer days = period.getDays();
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    private void setNotNullParamToEntity(TaskInDto taskInDto, Task task) {
        if (taskInDto.getName() != null) {
            task.setName(taskInDto.getName());
        }

        if (taskInDto.getDescription() != null) {
            task.setDescription(taskInDto.getDescription());
        }

        if (taskInDto.getProjectId() != null) {
            Project project = projectService.findById(taskInDto.getProjectId());
            setExecutorToTask(task, taskInDto, project);
        }

        if (taskInDto.getBasicPoints() != null) {
            task.setBasicPoints(taskInDto.getBasicPoints());
        }

        if (taskInDto.getPenaltyPoints() != null) {
            task.setPenaltyPoints(taskInDto.getPenaltyPoints());
        }

        if (taskInDto.getStatus() != null) {
            try {
                task.setStatus(EnumUtils.getEnum(TaskStatus.class, taskInDto.getStatus()));
            } catch (IllegalArgumentException exception) {
                throw new BadRequestException("Unknown status: " + taskInDto.getStatus());
            }
        }
    }

    private void setExecutorToTask(Task task, TaskInDto taskInDto, Project project) {
        if (taskInDto.getExecutorId() != null) {
            Employee employee = employeeService.getEmployee(taskInDto.getExecutorId());
            if (checkProjectContainsExecutor(project, employee)) {
                task.setExecutor(employeeService.getEmployee(taskInDto.getExecutorId()));
            } else {
                throw new BadRequestException(String.format("Сотрудника с id %d нет в проекте.",
                        taskInDto.getExecutorId()));
            }
        }
    }

    private boolean checkProjectContainsExecutor(Project project, Employee employee) {
        return project.getEmployees().contains(employee);
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
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Task.class)));
    }
}