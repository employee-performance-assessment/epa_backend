package ru.epa.epabackend.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeServiceImpl;
import ru.epa.epabackend.service.project.ProjectServiceImpl;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.exception.ExceptionDescriptions.*;

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
    private final ProjectServiceImpl projectService;
    private final EmployeeServiceImpl employeeService;
    private final ProjectMapper projectMapper;

    /**
     * Получение списка всех задач админом
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByAdmin() {
        return taskMapper.tasksToListOutDto(taskRepository.findAll());
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findByIdByAdmin(Long taskId) {
        return taskMapper.taskUpdateToOutDto(getTaskFromRepositoryById(taskId));
    }

    /**
     * Создание задачи админом
     */
    @Override
    public TaskFullDto createByAdmin(TaskInDto taskInDto, String email) {
        Employee admin = employeeService.getEmployeeByEmail(email);
        Project project = projectService.findById(taskInDto.getProjectId());
        projectService.checkUserAndProject(admin, project);
        Task task = taskMapper.dtoInToTask(taskInDto);
        task.setStatus(TaskStatus.NEW);
        task.setProject(project);
        task.setExecutor(employeeService.getEmployee(taskInDto.getExecutorId()));
        return taskMapper.taskCreateToOutDto(taskRepository.save(task));
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
        return taskMapper.taskUpdateToOutDto(taskRepository.save(task));
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
    public List<TaskFullDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        return taskRepository.findByProjectIdAndStatus(projectId, status).stream()
                .map(taskMapper::taskCreateToOutDto).collect(Collectors.toList());
    }

    /**
     * Получение задачи из репозитория по ID
     */
    private Task getTaskFromRepositoryById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND.getTitle()));
    }

    /**
     * Получение списка всех задач пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskShortDto> findAllByEmployeeId(Long employeeId, TaskStatus status) {
        if (status == null)
            return taskMapper.tasksToListOutDto(taskRepository.findAllByExecutorId(employeeId));
        else
            return taskRepository.findByExecutorIdAndStatus(employeeId, status).stream()
                    .map(taskMapper::taskShortToOutDto).collect(Collectors.toList());
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public TaskFullDto findById(Long employeeId, Long taskId) {
        return taskMapper.taskUpdateToOutDto(getTaskFromRepositoryByIdAndExecutorId(taskId, employeeId));
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
        return taskMapper.taskUpdateToOutDto(taskRepository.save(task));
    }

    /**
     * Получение задачи из репозитория по ID задачи и ID исполнителя
     */
    private Task getTaskFromRepositoryByIdAndExecutorId(Long taskId, Long employeeId) {
        return taskRepository.findByIdAndExecutorId(taskId, employeeId)
                .orElseThrow(() -> new NotFoundException(FORBIDDEN_TO_EDIT_NOT_YOUR_TASK.getTitle()));
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

        if (taskInDto.getExecutorId() != null) {
            Employee employee = employeeRepository.findById(taskInDto.getExecutorId())
                    .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND.getTitle()));
            task.setExecutor(employee);
        }

        if (taskInDto.getBasicPoints() != null) {
            task.setBasicPoints(taskInDto.getBasicPoints());
        }

        if (taskInDto.getProjectId() != null) {
            Project project = projectRepository.findById(taskInDto.getProjectId())
                    .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
            task.setProject(project);
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
}