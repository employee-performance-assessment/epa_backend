package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.task.TaskService;
import ru.epa.epabackend.util.EnumUtils;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Класс TaskEmployeeController содержит ендпоинты задач для не администратора.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Private: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("users/tasks")
@RequiredArgsConstructor
@Validated
public class TaskControllerEmployee {

    private final TaskService taskEmployeeService;
    private final EmployeeService employeeService;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника с возможной фильтрацией по статусу задачи.
     */
    @Operation(
            summary = "Получение всех задач по ID сотрудника с возможной фильрацией по статусу задачи",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
    public List<TaskShortDto> findAllTasksByEmployeeId(Principal principal,
                                                       @RequestParam(required = false) TaskStatus status) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        return taskEmployeeService.findAllByEmployeeId(employee.getId(), status);
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
    public TaskFullDto findTaskById(@Parameter(required = true) @PathVariable Long taskId,
                                    Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        return taskEmployeeService.findById(employee.getId(), taskId);
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(
            summary = "Обновление статуса выполнения задачи сотрудником"
    )
    @PatchMapping("/{taskId}")
    public TaskFullDto updateStatus(@Parameter(required = true) @PathVariable Long taskId,
                                    @Parameter(required = true) @RequestParam String status,
                                    Principal principal) {
        Employee employee = employeeService.getEmployeeByEmail(principal.getName());
        try {
            TaskStatus taskStatus = EnumUtils.getEnum(TaskStatus.class, status);
            return taskEmployeeService.updateStatus(employee.getId(), taskId, taskStatus);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Unknown status: " + status);
        }
    }

    /**
     * Эндпоинт получения списка задач проекта с определенным статусом задач
     */
    @Operation(
            summary = "Получение списка задач проекта с определенным статусом задач",
            description = "При успешном получении возвращается 200 Ok\n" +
                    "В случае отсутствия проекта с указанным id возвращается 404 Not Found"
    )
    @GetMapping("{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskFullDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                      @RequestParam TaskStatus status){
        return taskEmployeeService.findByProjectIdAndStatus(projectId, status);
    }
}