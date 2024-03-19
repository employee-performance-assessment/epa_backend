package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    List<Task> findAllByExecutorIdFilters(String status, Principal principal);

    /**
     * Найти задачу по ID
     */
    Task findByIdAndExecutorId(Principal principal, Long taskId);

    /**
     * Обновление задачи
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
}