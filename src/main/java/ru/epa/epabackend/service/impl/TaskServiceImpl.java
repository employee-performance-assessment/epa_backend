package ru.epa.epabackend.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskCreateFindByIdUpdateResponseDto;
import ru.epa.epabackend.dto.task.TaskCreateUpdateRequestDto;
import ru.epa.epabackend.dto.task.TaskFindAllResponseDto;
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
<<<<<<<<< Temporary merge branch 1
    public List<TaskShortDto> findAll() {
=========
    public List<TaskFindAllResponseDto> findAll() {
>>>>>>>>> Temporary merge branch 2
        return taskRepository.findAll().stream().map(taskMapper::mapToShortDto)
                .toList();
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto findDtoById(Long taskId) {
=========
    public TaskCreateFindByIdUpdateResponseDto findDtoById(Long taskId) {
>>>>>>>>> Temporary merge branch 2
        return taskMapper.mapToFullDto(findById(taskId));
    }

    /**
     * Создание задачи админом
     */
    @Override
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto create(TaskInDto taskInDto) {
        Project project = projectService.findById(taskInDto.getProjectId());
        Employee executor = employeeService.getEmployee(taskInDto.getExecutorId());
        taskInDto.setStatus("NEW");
        checkProjectContainsExecutor(project, executor);
        Task task = taskRepository.save(taskMapper.mapToEntity(taskInDto, project, executor));
=========
    public TaskCreateFindByIdUpdateResponseDto create(TaskCreateUpdateRequestDto taskCreateUpdateRequestDto) {
        Project project = projectService.findById(taskCreateUpdateRequestDto.getProjectId());
        Employee executor = employeeService.findById(taskCreateUpdateRequestDto.getExecutorId());
        taskCreateUpdateRequestDto.setStatus("NEW");
        checkProjectContainsExecutor(project, executor);
        Task task = taskRepository.save(taskMapper.mapToEntity(taskCreateUpdateRequestDto, project, executor));
>>>>>>>>> Temporary merge branch 2
        return taskMapper.mapToFullDto(task);

    }

    /**
     * Обновление задачи админом
     */
    @Override
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto update(Long taskId, TaskInDto taskInDto) {
        Task task = findById(taskId);
        setNotNullParamToEntity(taskInDto, task);
=========
    public TaskCreateFindByIdUpdateResponseDto update(
            Long taskId, TaskCreateUpdateRequestDto taskCreateUpdateRequestDto) {
        Task task = findById(taskId);
        setNotNullParamToEntity(taskCreateUpdateRequestDto, task);
>>>>>>>>> Temporary merge branch 2
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
    public List<TaskFindAllResponseDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findAllByProjectIdAndStatus(projectId, status)
                .stream().map(taskMapper::mapToShortDto).toList();
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
<<<<<<<<< Temporary merge branch 1
    public List<TaskShortDto> findAllByExecutorIdFilters(String status, Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
=========
    public List<TaskFindAllResponseDto> findAllByExecutorIdFilters(String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
>>>>>>>>> Temporary merge branch 2
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
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto findByIdAndExecutorId(Principal principal, Long taskId) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
=========
    public TaskCreateFindByIdUpdateResponseDto findByIdAndExecutorId(Principal principal, Long taskId) {
        Employee employee = employeeService.findByEmail(principal.getName());
>>>>>>>>> Temporary merge branch 2
        return taskMapper.mapToFullDto(findByIdAndExecutorId(taskId, employee.getId()));
    }

    /**
     * Обновление статуса задачи
     */
    @Override
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto updateStatus(Long taskId, String status, Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
=========
    public TaskCreateFindByIdUpdateResponseDto updateStatus(Long taskId, String status, Principal principal) {
        Employee employee = employeeService.findByEmail(principal.getName());
>>>>>>>>> Temporary merge branch 2
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

    private void setPointsToEmployeeAfterTaskDone(TaskCreateUpdateRequestDto dto, Task task) {
        Period period = Period.between(LocalDate.now(), dto.getDeadLine());
        Integer days = period.getDays();
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    private void setNotNullParamToEntity(TaskCreateUpdateRequestDto dto, Task task) {
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }

<<<<<<<<< Temporary merge branch 1
        if (taskInDto.getDescription() != null) {
            task.setDescription(taskInDto.getDescription());
        }

        if (taskInDto.getExecutorId() != null) {
            Employee employee = employeeService.getEmployee(taskInDto.getExecutorId());
            checkProjectContainsExecutor(task.getProject(), employee);
            task.setExecutor(employeeService.getEmployee(taskInDto.getExecutorId()));
=========
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }

        if (dto.getExecutorId() != null) {
            Employee employee = employeeService.findById(dto.getExecutorId());
            checkProjectContainsExecutor(task.getProject(), employee);
            task.setExecutor(employeeService.findById(dto.getExecutorId()));
>>>>>>>>> Temporary merge branch 2

        }

        if (dto.getBasicPoints() != null) {
            task.setBasicPoints(dto.getBasicPoints());
        }

<<<<<<<<< Temporary merge branch 1
        if (taskInDto.getPenaltyPoints() != null) {
            task.setPenaltyPoints(taskInDto.getPenaltyPoints());
        }

        if (taskInDto.getStatus() != null) {
=========
        if (dto.getPenaltyPoints() != null) {
            task.setPenaltyPoints(dto.getPenaltyPoints());
        }

        if (dto.getStatus() != null) {
>>>>>>>>> Temporary merge branch 2
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
<<<<<<<<< Temporary merge branch 1
}
=========
}
>>>>>>>>> Temporary merge branch 2
