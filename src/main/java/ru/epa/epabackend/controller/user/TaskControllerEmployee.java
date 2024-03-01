package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Private: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("/users/{employeeId}/tasks")
@RequiredArgsConstructor
@Validated
public class TaskControllerEmployee {

    private final TaskService taskEmployeeService;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника.
     */
    @Operation(
            summary = "Получение всех задач по ID сотрудника",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
    public List<TaskShortDto> findAllTasksByEmployeeId(@Parameter(required = true) @PathVariable Long employeeId) {
        return taskEmployeeService.findAllByEmployeeId(employeeId);
    }

    /**
     * Эндпойнт поиска задачи по ID сотрудника и ID задачи.
     */
    @Operation(
            summary = "Получение информации о задаче сотрудником",
            description = "Возвращает полную информацию о задаче, если она существует в базе данных." +
                    "В случае, если задачи не найдено, возвращает ошибкую 404"
    )
    @GetMapping("/{taskId}")
    public TaskFullDto findTaskById(@Parameter(required = true) @PathVariable Long employeeId,
                                    @Parameter(required = true) @PathVariable Long taskId) {
        return taskEmployeeService.findById(employeeId, taskId);
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(
            summary = "Обновление статуса выполнения задачи сотрудником"
    )
    @PatchMapping("/{taskId}")
    public TaskFullDto updateStatus(@Parameter(required = true) @PathVariable Long employeeId,
                                    @Parameter(required = true) @PathVariable Long taskId,
                                    @Parameter(required = true) @RequestParam String status)
            throws IllegalArgumentException {
        try {
            TaskStatus taskStatus = EnumUtils.getEnum(TaskStatus.class, status);
            return taskEmployeeService.updateStatus(employeeId, taskId, taskStatus);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Unknown status: " + status);
        }
    }

    @GetMapping("{projectId}")
    public List<TaskFullDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                      @RequestParam TaskStatus status){
        return taskEmployeeService.findByProjectIdAndStatus(projectId, status);
    }
}