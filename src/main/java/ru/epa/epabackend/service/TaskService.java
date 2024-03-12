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

<<<<<<<<< Temporary merge branch 1
    List<TaskShortDto> findAllByExecutorIdFilters(String status, Principal principal);
=========
    List<TaskFindAllResponseDto> findAllByExecutorIdFilters(String status, Principal principal);
>>>>>>>>> Temporary merge branch 2

    /**
     * Найти задачу по ID
     */
<<<<<<<<< Temporary merge branch 1
    TaskFullDto findByIdAndExecutorId(Principal principal, Long taskId);
=========
    TaskCreateFindByIdUpdateResponseDto findByIdAndExecutorId(Principal principal, Long taskId);
>>>>>>>>> Temporary merge branch 2

    /**
     * Обновление задачи
     */
<<<<<<<<< Temporary merge branch 1
    TaskFullDto updateStatus(Long taskId, String status, Principal principal);
=========
    TaskCreateFindByIdUpdateResponseDto updateStatus(Long taskId, String status, Principal principal);
>>>>>>>>> Temporary merge branch 2

    /**
     * Получение списка всех задач
     */
<<<<<<<<< Temporary merge branch 1
    List<TaskShortDto> findAll();
=========
    List<TaskFindAllResponseDto> findAll();
>>>>>>>>> Temporary merge branch 2

    /**
     * Создание задачи
     */
<<<<<<<<< Temporary merge branch 1
    TaskFullDto create(TaskInDto taskDto);
=========
    TaskCreateFindByIdUpdateResponseDto create(TaskCreateUpdateRequestDto taskDto);
>>>>>>>>> Temporary merge branch 2

    /**
     * Найти задачу по ID
     */
<<<<<<<<< Temporary merge branch 1
    TaskFullDto findDtoById(Long taskId);
=========
    TaskCreateFindByIdUpdateResponseDto findDtoById(Long taskId);
>>>>>>>>> Temporary merge branch 2

    /**
     * Обновление задачи
     */
<<<<<<<<< Temporary merge branch 1
    TaskFullDto update(Long taskId, TaskInDto taskDto);
=========
    TaskCreateFindByIdUpdateResponseDto update(Long taskId, TaskCreateUpdateRequestDto taskDto);
>>>>>>>>> Temporary merge branch 2

    /**
     * Удаление задачи
     */
    void delete(Long taskId);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<TaskFindAllResponseDto> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}