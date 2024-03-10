package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    List<TaskShortDto> findAllByExecutorIdFilters(String status, Principal principal);

    /**
     * Найти задачу по ID
     */
    TaskFullDto findByIdAndExecutorId(Principal principal, Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullDto updateStatus(Long taskId, String status, Principal principal);

    /**
     * Получение списка всех задач
     */
    List<TaskShortDto> findAll();

    /**
     * Создание задачи
     */
    TaskFullDto create(TaskInDto taskDto);

    /**
     * Найти задачу по ID
     */
    TaskFullDto findDtoById(Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullDto update(Long taskId, TaskInDto taskDto);

    /**
     * Удаление задачи
     */
    void delete(Long taskId);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<TaskShortDto> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}