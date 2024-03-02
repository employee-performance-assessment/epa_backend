package ru.epa.epabackend.service.task;

import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.util.TaskStatus;

import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    /**
     * Получение списка всех задач
     */
    List<TaskShortDto> findAllByEmployeeId(Long userId);

    /**
     * Найти задачу по ID
     */
    TaskFullDto findById(Long userId, Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullDto updateStatus(Long taskId, Long userId, TaskStatus taskStatus);

    /**
     * Получение списка всех задач
     */
    List<TaskShortDto> findAllByAdmin();

    /**
     * Создание задачи
     */
    TaskFullDto createByAdmin(TaskInDto taskDto);

    /**
     * Найти задачу по ID
     */
    TaskFullDto findByIdByAdmin(Long taskId);

    /**
     * Обновление задачи
     */
    TaskFullDto updateByAdmin(Long taskId, TaskInDto taskDto);

    /**
     * Удаление задачи
     */
    void deleteByAdmin(Long taskId);
}