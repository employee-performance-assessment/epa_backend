package ru.epa.epabackend.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
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
    public List<TaskShortResponseDto> findAll() {
        return taskRepository.findAll().stream().map(taskMapper::mapToShortDto)
                .toList();
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullResponseDto findDtoById(Long taskId) {
        return taskMapper.mapToFullDto(findById(taskId));
    }

    /**
     * Создание задачи админом
     */
    @Override
    public TaskFullResponseDto create(TaskRequestDto taskCreateUpdateRequestDto) {
        Project project = projectService.findById(taskCreateUpdateRequestDto.getProjectId());
        Employee executor = employeeService.findById(taskCreateUpdateRequestDto.getExecutorId());
        taskCreateUpdateRequestDto.setStatus("NEW");
        checkProjectContainsExecutor(project, executor);
        Task task = taskRepository.save(taskMapper.mapToEntity(taskCreateUpdateRequestDto, project, executor));
        return taskMapper.mapToFullDto(task);

    }

    /**
     * Обновление задачи админом
     */
    @Override
    public TaskFullResponseDto update(
            Long taskId, TaskRequestDto taskCreateUpdateRequestDto) {
        Task task = findById(taskId);
        setNotNullParamToEntity(taskCreateUpdateRequestDto, task);
        if (task.getStatus() == TaskStatus.DONE) {
            setPointsToEmployeeAfterTaskDone(taskCreateUpdateRequestDto, task);
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
    public List<TaskShortResponseDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findAllByProjectIdAndStatus(projectId, status)
                .stream().map(taskMapper::mapToShortDto).toList();
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortResponseDto> findAllByExecutorIdFilters(String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
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
    public TaskFullResponseDto findByIdAndExecutorId(Principal principal, Long taskId) {
        Employee employee = employeeService.findByEmail(principal.getName());
        return taskMapper.mapToFullDto(findByIdAndExecutorId(taskId, employee.getId()));
    }

    /**
     * Обновление статуса задачи
     */
    @Override
    public TaskFullResponseDto updateStatus(Long taskId, String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
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

    private void setPointsToEmployeeAfterTaskDone(TaskRequestDto dto, Task task) {
        Period period = Period.between(LocalDate.now(), dto.getDeadLine());
        Integer days = period.getDays();
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    private void setNotNullParamToEntity(TaskRequestDto dto, Task task) {
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }

        if (dto.getExecutorId() != null) {
            Employee employee = employeeService.findById(dto.getExecutorId());
            checkProjectContainsExecutor(task.getProject(), employee);
            task.setExecutor(employeeService.findById(dto.getExecutorId()));

        }

        if (dto.getBasicPoints() != null) {
            task.setBasicPoints(dto.getBasicPoints());
        }

        if (dto.getPenaltyPoints() != null) {
            task.setPenaltyPoints(dto.getPenaltyPoints());
        }

        if (dto.getStatus() != null) {
            try {
                task.setStatus(EnumUtils.getEnum(TaskStatus.class, dto.getStatus()));
            } catch (IllegalArgumentException exception) {
                throw new BadRequestException("Unknown status: " + dto.getStatus());
            }
        }
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
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Task.class)));
    }
}