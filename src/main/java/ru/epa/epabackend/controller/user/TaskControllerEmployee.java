package ru.epa.epabackend.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.service.task.TaskService;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.util.List;

/**
 * Класс TaskEmployeeController содержит ендпоинты задач для не администратора.
 *
 * @author Владислав Осипов
 */
@RestController
@RequestMapping("/users/{employeeId}/tasks")
@RequiredArgsConstructor
@Validated
public class TaskControllerEmployee {

    private final TaskService taskEmployeeService;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника.
     */
    @GetMapping
    public List<TaskShortDto> findAllTasksByEmployeeId(@PathVariable Long employeeId) {
        return taskEmployeeService.findAllByEmployeeId(employeeId);
    }

    /**
     * Эндпойнт поиска задачи по ID сотрудника и ID задачи.
     */
    @GetMapping("/{taskId}")
    public TaskFullDto findTaskById(@PathVariable Long employeeId, @PathVariable Long taskId) {
        return taskEmployeeService.findById(employeeId, taskId);
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @PatchMapping("/{taskId}")
    public TaskFullDto updateStatus(@PathVariable Long employeeId, @PathVariable Long taskId,
                                    @RequestParam String status) throws IllegalArgumentException {
        try{
            TaskStatus taskStatus = EnumUtils.getEnum(TaskStatus.class, status);
            return taskEmployeeService.updateStatus(employeeId, taskId, taskStatus);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Unknown status: " + status);
        }
    }
}