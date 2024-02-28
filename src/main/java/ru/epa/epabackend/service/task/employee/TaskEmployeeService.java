package ru.epa.epabackend.service.task.employee;

import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.util.TaskStatus;

import java.util.List;

/**
 * Интерфейс TaskAdminService содержит методы действий с задачами для администратора.
 *
 * @author Владислав Осипов
 */
public interface TaskEmployeeService {

    /**
     * Получение списка всех задач
     */
    List<TaskOutDto> findAllByEmployeeId(Long userId);

    /**
     * Найти задачу по ID
     */
    TaskOutDto findById(Long userId, Long taskId);

    /**
     * Обновление задачи
     */
    TaskOutDto update(Long taskId, Long userId, TaskStatus taskStatus);
}