package ru.epa.epabackend.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.RequestTaskDto;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Класс TaskServiceImpl содержит методы действий с задачами.
 *
 * @author Владислав Осипов
 */
@Slf4j
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
        log.info("Получение списка всех задач админом");
        return taskRepository.findAllByOwnerEmail(email);
    }

    /**
     * Получение списка всех задач админом по определенному проекту админом
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByProjectId(String email, Long projectId) {
        Employee admin = employeeService.findByEmail(email);
        Project project = projectService.findById(projectId);
        projectService.checkUserAndProject(admin, project);
        return taskRepository.findAllByProjectId(projectId);
    }

    /**
     * Найти задачу по ID админом
     */
    @Override
    @Transactional(readOnly = true)
    public Task findDtoById(Long taskId, String email) {
        log.info("Найти задачу с идентификатором {} по ID админом", taskId);
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
    public Task create(RequestTaskDto requestTaskDto, String email) {
        log.info("Создание задачи {} админом", requestTaskDto.getName());
        Project project = projectService.findById(requestTaskDto.getProjectId());
        Employee executor = employeeService.findById(requestTaskDto.getExecutorId());
        Employee admin = employeeService.findByEmail(email);
        employeeService.checkAdminForEmployee(admin, executor);
        projectService.checkUserAndProject(admin, project);
        requestTaskDto.setStatus("NEW");
        return taskRepository.save(taskMapper.mapToEntity(requestTaskDto, project, executor, admin));
    }

    /**
     * Обновление задачи админом
     */
    @Override
    public Task update(Long taskId, RequestTaskDto requestTaskDto, String email) {
        log.info("Обновление задачи с идентификатором {} админом", taskId);
        Task oldTask = findById(taskId);
        Project project = oldTask.getProject();
        Employee admin = employeeService.findByEmail(email);
        if (!taskRepository.existsByIdAndOwnerEmail(taskId, email)) {
            throw new BadRequestException("Администратор не является автором задачи");
        }
        projectService.checkUserAndProject(admin, project);
        Employee executor = checkExecutor(requestTaskDto, oldTask);
        taskMapper.updateFields(requestTaskDto, project, executor, oldTask);
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
        log.info("Удаление задачи с идентификатором {} админом", taskId);
        Task task = findById(taskId);
        if (task.getStatus() == TaskStatus.DONE) {
            throw new BadRequestException("Администратор не может удалить выполненную задачу");
        }
        Project project = task.getProject();
        Employee admin = employeeService.findByEmail(email);
        projectService.checkUserAndProject(admin, project);
        taskRepository.delete(task);
    }

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, String email) {
        log.info("Получение списка задач проекта с идентификатором {} с определенным статусом {} задач",
                projectId, status);
        Project project = projectService.findById(projectId);
        Employee employee = employeeService.findByEmail(email);
        Employee admin = employee.getCreator() == null ? employee : employee.getCreator();
        projectService.checkUserAndProject(admin, project);
        return taskRepository.findAllByProjectIdAndStatus(projectId, status);
    }

    /**
     * Получение списка всех задач пользователя
     */
    @Transactional(readOnly = true)
    public List<Task> findAllByExecutorEmail(Principal principal, String text) {
        Employee employee = employeeService.findByEmail(principal.getName());
        log.info("Получение списка всех задач пользователя с идентификатором {}", employee.getId());
        return taskRepository.findAllByExecutorIdAndText(employee.getId(), text);
    }

    /**
     * Получение списка всех задач пользователя с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByExecutorEmailAndStatus(String status, Principal principal, String text) {
        Employee employee = employeeService.findByEmail(principal.getName());
        log.info("Получение списка всех задач пользователя с идентификатором {} с указанным статусом {} задач",
                employee.getId(), status);
        try {
            return taskRepository.findAllByExecutorIdAndStatusAndText(employee.getId(), getTaskStatus(status), text);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Указан неверный статус задачи");
        }
    }

    /**
     * Получение списка всех задач пользователя админом
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByEmployeeId(Long employeeId, String email, String text) {
        log.info("Получение списка всех задач пользователя с идентификатором {} админом", employeeId);
        return taskRepository.findTasksByOwnerEmailAndExecutorIdAndText(
                email, employeeId, text);
    }

    /**
     * Получение списка всех задач пользователя админом с указанным статусом задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllByEmployeeIdAndStatus(Long employeeId, String email, String status, String text) {
        log.info("Получение списка всех задач пользователя с идентификатором {} админом" +
                " указанным статусом {} задач", employeeId, status);
        try {
            return taskRepository.findTasksByOwnerEmailAndExecutorIdAndStatusAndText(
                    email, employeeId, getTaskStatus(status), text);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Указан неверный статус задачи");
        }
    }

    /**
     * Найти задачу по ID сотрудником
     */
    @Override
    @Transactional(readOnly = true)
    public Task findByIdAndOwnerId(Principal principal, Long taskId) {
        log.info("Получение задачи из репозитория по идентификатору задачи {} " +
                "и по идентификатору админа сотрудника с email {}", taskId, principal.getName());
        Employee employee = employeeService.findByEmail(principal.getName());
        Employee admin = employee.getCreator() == null ? employee : employee.getCreator();
        return taskRepository.findByIdAndOwnerId(taskId, admin.getId()).orElseThrow(() ->
                new EntityNotFoundException("Задача не найдена"));
    }

    /**
     * Обновление статуса задачи
     */
    @Override
    public Task updateStatus(Long taskId, String status, Principal principal) {
        log.info("Обновление статуса задачи с идентификатором {}", taskId);
        Employee employee = employeeService.findByEmail(principal.getName());
        Long adminId = employee.getCreator() == null ? employee.getId() : employee.getCreator().getId();
        try {
            TaskStatus taskStatus = getTaskStatus(status);
            Task task = taskRepository.findByIdAndOwnerId(taskId, adminId).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Не найдена задача для исполнителя %s",
                            employee.getFullName())));
            if (taskStatus == TaskStatus.IN_PROGRESS) {
                task.setStartDate(LocalDate.now());
            }
            if (taskStatus == TaskStatus.DONE) {
                setPointsToEmployeeAfterTaskDone(task);
                task.setFinishDate(LocalDate.now());
            }
            task.setStatus(taskStatus);
            return taskRepository.save(task);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Указан неверный статус задачи");
        }
    }

    /**
     * Проставление очков после того как задача выполнена
     */
    private void setPointsToEmployeeAfterTaskDone(Task task) {
        log.info("Проставление очков после того как задача {} выполнена", task);
        Integer days = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadLine()));
        task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
    }

    /**
     * Получение корректного статуса задачи
     */
    private TaskStatus getTaskStatus(String status) {
        log.info("Получение корректного статуса задачи");
        TaskStatus taskStatus = null;
        if (status != null) {
            taskStatus = EnumUtils.getEnum(TaskStatus.class, status);
        }
        return taskStatus;
    }

    /**
     * Получение задачи из репозитория по ID
     */
    @Override
    @Transactional(readOnly = true)
    public Task findById(Long taskId) {
        log.info("Получение задачи из репозитория по идентификатору {}", taskId);
        return taskRepository.findById(taskId).orElseThrow(() ->
                new EntityNotFoundException("Задача не найдена"));
    }

    /**
     * Получение списка всех задач команды сотрудником по определенному проекту
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllForEmployeeByProjectId(String email, Long projectId) {
        log.info("Получение списка всех задач команды сотрудником");
        Employee employee = employeeService.findByEmail(email);
        Employee admin = employee.getCreator();
        if (admin == null) {
            findAllByProjectId(employee.getEmail(), projectId);
        }
        Project project = projectService.findById(projectId);
        projectService.checkUserAndProject(admin, project);
        return taskRepository.findAllByProjectId(projectId);
    }

    /**
     * Получение списка всех задач команды сотрудником
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllForEmployee(String email) {
        log.info("Получение списка всех задач команды сотрудником");
        Employee employee = employeeService.findByEmail(email);
        Employee admin = employee.getCreator();
        if (admin == null) {
            return findAll(employee.getEmail());
        }
        return taskRepository.findAllByOwnerId(admin.getId());
    }

    /**
     * Проверка исполнителя задач при обновлении задачи. Если исполнитель поменялся, то
     * ищем его айди в репозитории, если находим, то возвращаем его. Если не найден, то
     * берем из задачи старого исполнителя.
     */
    private Employee checkExecutor(RequestTaskDto requestTaskDto, Task oldTask) {
        log.info("Проверка исполнителя задач при обновлении задачи");
        Employee executor;
        if (requestTaskDto.getExecutorId() != null) {
            executor = employeeService.findById(requestTaskDto.getExecutorId());
        } else {
            executor = oldTask.getExecutor();
        }
        return executor;
    }
}
