package ru.epa.epabackend.controller.task.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.service.task.employee.TaskEmployeeService;
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
public class TaskEmployeeController {

    private final TaskEmployeeService taskEmployeeService;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника.
     */
    @GetMapping
    public List<TaskOutDto> findAllTasksByEmployeeId(
            @PathVariable Long employeeId
    ) {
        return taskEmployeeService.findAllByEmployeeId(employeeId);
    }

    /**
     * Эндпойнт поиска задачи по ID сотрудника и ID задачи.
     */
    @GetMapping("/{taskId}")
    public TaskOutDto findTaskById(
            @PathVariable Long employeeId,
            @PathVariable Long taskId
    ) {

        return taskEmployeeService.findById(employeeId, taskId);
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @PatchMapping("/{taskId}")
    public TaskOutDto updateTask(
            @PathVariable Long employeeId,
            @PathVariable Long taskId,
            @RequestParam String status) {
        TaskStatus taskStatus = TaskStatus.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + status));
        return taskEmployeeService.update(employeeId, taskId, taskStatus);
    }
}