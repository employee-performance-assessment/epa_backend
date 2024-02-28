package ru.epa.epabackend.service.task.employee.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.task.employee.TaskEmployeeService;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.List;

import static ru.epa.epabackend.exception.ExceptionDescriptions.FORBIDDEN_TO_EDIT_NOT_YOUR_TASK;

/**
 * Класс TaskEmployeeServiceImpl содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskEmployeeServiceImpl implements TaskEmployeeService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Получение списка всех задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskOutDto> findAllByEmployeeId(Long employeeId) {
        List<Task> tasks = taskRepository.findAllByExecutorId(employeeId);
        return taskMapper.tasksToListOutDto(tasks);
    }

    /**
     * Найти задачу по ID
     */
    @Override
    @Transactional(readOnly = true)
    public TaskOutDto findById(Long employeeId, Long taskId) {
        return taskMapper.taskUpdateToOutDto(getTaskFromRepositoryByIdAndExecutorId(taskId, employeeId));
    }

    /**
     * Обновление задачи
     */
    @Override
    public TaskOutDto update(Long employeeId, Long taskId, TaskStatus taskStatus) {
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
}