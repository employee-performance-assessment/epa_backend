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
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.service.TaskService;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    /**
     * Получение списка всех задач админом
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByAdmin() {
        return taskRepository.findAll().stream().map(taskMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findByIdByAdmin(Long taskId) {
        return taskMapper.mapToFullDto(getTaskFromRepositoryById(taskId));
    }

    /**
     * Создание задачи админом
     */
    @Override
    public TaskFullDto createByAdmin(TaskInDto taskInDto) {
        Project project = projectService.findById(taskInDto.getProjectId());
        Task task = taskMapper.mapToEntity(taskInDto);
        task.setStatus(TaskStatus.NEW);
        task.setProject(project);
        setExecutorToTask(task, taskInDto, project);
        return taskMapper.mapToFullDto(taskRepository.save(task));
    }

    /**
     * Обновление задачи админом
     */
    @Override
    public TaskFullDto updateByAdmin(Long taskId, TaskInDto taskInDto) {
        Task task = getTaskFromRepositoryById(taskId);
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
    public void deleteByAdmin(Long taskId) {
        taskRepository.delete(getTaskFromRepositoryById(taskId));
    }

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    @Override
    public List<TaskShortDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findByProjectIdAndStatus(projectId, status)
                .stream().map(taskMapper::mapToShortDto).collect(Collectors.toList());
    }

    /**
     * Получение задачи из репозитория по ID
     */
    private Task getTaskFromRepositoryById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Task.class)));
    }

    /**
     * Получение списка всех задач пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByEmployeeId(Long employeeId) {
        return taskRepository.findAllByExecutorId(employeeId).stream()
                .map(taskMapper::mapToShortDto).collect(Collectors.toList());
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByEmployeeIdAndStatus(Long employeeId, TaskStatus status) {
        return taskRepository.findByExecutorIdAndStatus(employeeId, status).stream()
                .map(taskMapper::mapToShortDto).collect(Collectors.toList());
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findById(Long employeeId, Long taskId) {
        return taskMapper.mapToFullDto(getTaskFromRepositoryByIdAndExecutorId(taskId, employeeId));
    }

    /**
     * Обновление задачи
     */
    @Override
    public TaskFullDto updateStatus(Long employeeId, Long taskId, TaskStatus taskStatus) {
        Task task = getTaskFromRepositoryByIdAndExecutorId(taskId, employeeId);
        if (taskStatus == TaskStatus.IN_PROGRESS) {
            task.setStartDate(LocalDate.now());
        }
        task.setStatus(taskStatus);
        return taskMapper.mapToFullDto(taskRepository.save(task));
    }

    /**
     * Получение задачи из репозитория по ID задачи и ID исполнителя
     */
    private Task getTaskFromRepositoryByIdAndExecutorId(Long taskId, Long employeeId) {
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
            task.setName(taskInDto.getDescription());
        }

        if (taskInDto.getProjectId() != null) {
            Project project = projectService.findById(taskInDto.getProjectId());
            task.setProject(project);
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
        if (checkExecutorExists(taskInDto)) {
            Employee employee = employeeService.getEmployee(taskInDto.getExecutorId());
            if (checkProjectContainsExecutor(project, employee)) {
                task.setExecutor(employeeService.getEmployee(taskInDto.getExecutorId()));
            } else {
                throw new BadRequestException(String.format("Сотрудника с id %d нет в проекте.",
                        taskInDto.getExecutorId()));
            }
        }
    }

    private boolean checkExecutorExists(TaskInDto taskInDto) {
        return taskInDto.getExecutorId() != null;
    }

    private boolean checkProjectContainsExecutor(Project project, Employee employee) {
        return project.getEmployees().contains(employee);
    }
}