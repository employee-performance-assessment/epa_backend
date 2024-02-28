package ru.epa.epabackend.service.task.admin;

import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.dto.TaskOutDto;

import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskAdminService {

    /**
     * Получение списка всех задач
     */
    List<TaskOutDto> findAll();

    /**
     * Создание задачи
     */
    TaskOutDto create(TaskInDto taskDto);

    /**
     * Найти задачу по ID
     */
    TaskOutDto findById(Long taskId);

    /**
     * Обновление задачи
     */
    TaskOutDto update(Long taskId, TaskInDto taskDto);

    /**
     * Удаление задачи
     */
    void delete(Long taskId);
}