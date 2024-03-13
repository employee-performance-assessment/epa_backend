package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    List<TaskShortResponseDto> findAllByExecutorIdFilters(String status, Principal principal);

    /**
     * Найти задачу по ID
     */
    TaskFullResponseDto findByIdAndExecutorId(Principal principal, Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullResponseDto updateStatus(Long taskId, String status, Principal principal);

    /**
     * Получение списка всех задач
     */
    List<TaskShortResponseDto> findAll();

    /**
     * Создание задачи
     */
    TaskFullResponseDto create(TaskRequestDto taskDto);

    /**
     * Найти задачу по ID
     */
    TaskFullResponseDto findDtoById(Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullResponseDto update(Long taskId, TaskRequestDto taskDto);

    /**
     * Удаление задачи
     */
    void delete(Long taskId);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<TaskShortResponseDto> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}