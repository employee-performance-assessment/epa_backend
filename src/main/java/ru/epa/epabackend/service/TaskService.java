package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.task.TaskCreateFindByIdUpdateResponseDto;
import ru.epa.epabackend.dto.task.TaskCreateUpdateRequestDto;
import ru.epa.epabackend.dto.task.TaskFindAllResponseDto;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    List<TaskFindAllResponseDto> findAllByExecutorIdFilters(String status, Principal principal);

    /**
     * Найти задачу по ID
     */
    TaskCreateFindByIdUpdateResponseDto findByIdAndExecutorId(Principal principal, Long taskId);

    /**
     * Обновление задачи
     */
    TaskCreateFindByIdUpdateResponseDto updateStatus(Long taskId, String status, Principal principal);

    /**
     * Получение списка всех задач
     */
    List<TaskFindAllResponseDto> findAll();

    /**
     * Создание задачи
     */
    TaskCreateFindByIdUpdateResponseDto create(TaskCreateUpdateRequestDto taskDto);

    /**
     * Найти задачу по ID
     */
    TaskCreateFindByIdUpdateResponseDto findDtoById(Long taskId);

    /**
     * Обновление задачи
     */
    TaskCreateFindByIdUpdateResponseDto update(Long taskId, TaskCreateUpdateRequestDto taskDto);
    
    /**
     * Удаление задачи
     */
    void delete(Long taskId);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<TaskFindAllResponseDto> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}
