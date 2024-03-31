package ru.epa.epabackend.service;

import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskService содержит методы действий с задачами .
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    /**
     * Получение списка задач по ID исполнителя и статусу задачи
     */
    List<Task> findAllByExecutorIdFilters(String status, Principal principal);

    /**
     * Получение списка задач администратором по ID исполнителя и статусу задачи
     */
    List<Task> findAllByEmployeeId(Long employeeId, String email);

    /**
     * Найти задачу по ID задачи и ID исполнителя
     */
    Task findByIdAndExecutorId(Principal principal, Long taskId);

    /**
     * Обновление статуса задачи
     */
    Task updateStatus(Long taskId, String status, Principal principal);

    /**
     * Получение списка всех задач
     */
    List<Task> findAll(String email);

    /**
     * Создание задачи
     */
    Task create(TaskRequestDto taskDto, String email);

    /**
     * Найти задачу по ID
     */
    Task findDtoById(Long taskId, String email);

    /**
     * Обновление задачи
     */
    Task update(Long taskId, TaskRequestDto taskDto, String email);

    /**
     * Удаление задачи
     */
    void delete(Long taskId, String email);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    @Transactional(readOnly = true)
    Task findByIdAndExecutorId(Long taskId, Long employeeId);

    @Transactional(readOnly = true)
    Task findById(Long taskId);
}