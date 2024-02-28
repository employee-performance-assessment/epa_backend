package ru.epa.epabackend.service.task.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.task.admin.TaskAdminService;
import ru.epa.epabackend.util.TaskStatus;
import ru.epa.epabackend.util.Utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static ru.epa.epabackend.exception.ExceptionDescriptions.*;

/**
 * Класс TaskAdminServiceImpl содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskAdminServiceImpl implements TaskAdminService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    /**
     * Получение списка всех задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskOutDto> findAll() {
        return taskMapper.tasksToListOutDto(taskRepository.findAll());
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public TaskOutDto findById(Long taskId) {
        return taskMapper.taskUpdateToOutDto(getTaskFromRepositoryById(taskId));
    }

    /**
     * Создание задачи
     */
    @Override
    public TaskOutDto create(TaskInDto taskInDto) {
        Project project = getProjectFromRepositoryById(taskInDto);
        Task task = taskMapper.dtoInToTask(taskInDto);
        task.setStatus(TaskStatus.NEW);
        task.setProject(project);
        task.setCreator(getEmployeeFromRepositoryById(taskInDto.getCreatorId()));
        task.setExecutor(getEmployeeFromRepositoryById(taskInDto.getExecutorId()));
        return taskMapper.taskCreateToOutDto(taskRepository.save(task));
    }

    /**
     * Обновление задачи
     */
    @Override
    public TaskOutDto update(Long taskId, TaskInDto taskInDto) {
        Task task = getTaskFromRepositoryById(taskId);
        Utils.setNotNullParamToEntity(taskInDto, task, employeeRepository, projectRepository);
        if (task.getStatus() == TaskStatus.DONE) {
            Period period = Period.between(LocalDate.now(), taskInDto.getFinishDate());
            Integer days = period.getDays();
            task.setPoints(task.getBasicPoints() + days * task.getPenaltyPoints());
        }
        return taskMapper.taskUpdateToOutDto(taskRepository.save(task));
    }

    /**
     * Удаление задачи
     */
    @Override
    public void delete(Long taskId) {
        taskRepository.delete(getTaskFromRepositoryById(taskId));
    }

    /**
     * Получение пользователя из репозитория по ID
     */
    private Employee getEmployeeFromRepositoryById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND.getTitle()));
    }

    /**
     * Получение задачи из репозитория по ID
     */
    private Task getTaskFromRepositoryById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND.getTitle()));
    }

    /**
     * Получение проекта из репозитория по ID
     */
    private Project getProjectFromRepositoryById(TaskInDto taskInDto) {
        return projectRepository.findById(taskInDto.getProjectId())
                .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
    }
}