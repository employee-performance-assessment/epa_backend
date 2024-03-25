package ru.epa.epabackend.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeShortAnalyticsResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.task.TaskAnalyticsFullResponseDto;
import ru.epa.epabackend.dto.task.TaskAnalyticsShortResponseDto;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.service.TaskService;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
    private final EmployeeMapper employeeMapper;
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
     * Получение командной статистики для админа
     */
    @Override
    public TaskAnalyticsFullResponseDto findTeamStatisticsByAdmin(String rangeStart, String rangeEnd, String email) {
        double teamCompletedOnTime = 0;
        double teamNotCompletedOnTime = 0;
        int completedOnTime = 0;
        int notCompletedOnTime = 0;
        List<EmployeeShortResponseDto> leaders = new ArrayList<>();
        List<EmployeeShortResponseDto> deadlineViolators = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(rangeStart, DateConstant.DATE_SPACE);
        LocalDate endDate = LocalDate.parse(rangeEnd, DateConstant.DATE_SPACE);
        Employee admin = employeeService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByOwnerId(admin.getId(), startDate, endDate);
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                notCompletedOnTime++;
                teamNotCompletedOnTime++;
            } else {
                completedOnTime++;
                teamCompletedOnTime++;
            }

            if ((completedOnTime / (completedOnTime + notCompletedOnTime)) * 100 > 50) {
                leaders.add(employeeMapper.mapToShortDto(task.getExecutor()));
            } else {
                deadlineViolators.add(employeeMapper.mapToShortDto(task.getExecutor()));
            }
            completedOnTime = 0;
            notCompletedOnTime = 0;
        }

        double totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamNotCompletedOnTime;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return taskMapper.mapToAnalyticsDto(
                    teamCompletedOnTime / totalNumbersOfTaskOfTeamCompleted * 100,
                    teamNotCompletedOnTime / totalNumbersOfTaskOfTeamCompleted * 100,
                    leaders,
                    deadlineViolators);
        } else {
            return new TaskAnalyticsFullResponseDto();
        }
    }

    /**
     * Получение индивидуальной статистики для админа
     */
    @Override
    public List<EmployeeShortAnalyticsResponseDto> findIndividualStatisticsByAdmin(String rangeStart, String rangeEnd,
                                                                                   String email) {
        double completedOnTime = 0;
        double notCompletedOnTime = 0;
        List<EmployeeShortAnalyticsResponseDto> employeesShortDto = new ArrayList<>();
        Employee admin = employeeService.findByEmail(email);
        LocalDate startDate = LocalDate.parse(rangeStart, DateConstant.DATE_SPACE);
        LocalDate endDate = LocalDate.parse(rangeEnd, DateConstant.DATE_SPACE);
        List<Employee> employees = employeeService.findAllByCreatorId(admin.getId());
        for (Employee employee : employees) {
            for (Task task : employee.getTasks()) {
                if (isFinishDateBetweenStartDateAndEndDate(task, startDate, endDate)) {
                    if (task.getFinishDate().isAfter(task.getDeadLine())) {
                        notCompletedOnTime++;
                    } else {
                        completedOnTime++;
                    }
                }
            }

            double totalNumberOfTaskOfEmployeeCompleted = completedOnTime + notCompletedOnTime;
            if (totalNumberOfTaskOfEmployeeCompleted != 0) {
                employeesShortDto.add(employeeMapper.mapToShortAnalyticsDto(
                        employee,
                        completedOnTime / totalNumberOfTaskOfEmployeeCompleted * 100,
                        notCompletedOnTime / totalNumberOfTaskOfEmployeeCompleted * 100));
            }
            completedOnTime = 0;
            notCompletedOnTime = 0;
        }
        return employeesShortDto;
    }

    /**
     * Получение командной статистики для сотрудника
     */
    @Override
    public TaskAnalyticsShortResponseDto findTeamStatistics(String rangeStart, String rangeEnd, String email) {
        double teamCompletedOnTime = 0;
        double teamNotCompletedOnTime = 0;
        Employee employee = employeeService.findByEmail(email);
        LocalDate startDate = LocalDate.parse(rangeStart, DateConstant.DATE_SPACE);
        LocalDate endDate = LocalDate.parse(rangeEnd, DateConstant.DATE_SPACE);
        List<Task> tasks = taskRepository.findAllByOwnerId(employee.getCreator().getId(), startDate, endDate);
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                teamNotCompletedOnTime++;
            } else {
                teamCompletedOnTime++;
            }
        }

        double totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamNotCompletedOnTime;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return taskMapper.mapToAnalyticsDto(
                    teamCompletedOnTime / totalNumbersOfTaskOfTeamCompleted * 100,
                    teamNotCompletedOnTime / totalNumbersOfTaskOfTeamCompleted * 100);
        } else {
            return new TaskAnalyticsShortResponseDto();
        }
    }

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    @Override
    public EmployeeShortAnalyticsResponseDto findIndividualStatistics(String rangeStart, String rangeEnd,
                                                                      String email) {
        double completedOnTime = 0;
        double notCompletedOnTime = 0;
        Employee employee = employeeService.findByEmail(email);
        LocalDate startDate = LocalDate.parse(rangeStart, DateConstant.DATE_SPACE);
        LocalDate endDate = LocalDate.parse(rangeEnd, DateConstant.DATE_SPACE);
        for (Task task : employee.getTasks()) {
            if (isFinishDateBetweenStartDateAndEndDate(task, startDate, endDate)) {
                if (task.getFinishDate().isAfter(task.getDeadLine())) {
                    notCompletedOnTime++;
                } else {
                    completedOnTime++;
                }
            }
        }

        double totalNumberOfTaskOfEmployeeCompleted = completedOnTime + notCompletedOnTime;
        if (totalNumberOfTaskOfEmployeeCompleted != 0) {
            return employeeMapper.mapToShortAnalyticsDto(
                    employee,
                    completedOnTime / totalNumberOfTaskOfEmployeeCompleted * 100,
                    notCompletedOnTime / totalNumberOfTaskOfEmployeeCompleted * 100);
        } else {
            return new EmployeeShortAnalyticsResponseDto();
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

    private boolean isFinishDateBetweenStartDateAndEndDate(Task task, LocalDate startDate, LocalDate endDate) {
        return task.getFinishDate() != null &&
                task.getFinishDate().isAfter(startDate) &&
                task.getFinishDate().isBefore(endDate);
    }
}