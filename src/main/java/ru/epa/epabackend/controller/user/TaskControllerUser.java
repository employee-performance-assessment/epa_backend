package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.service.TaskService;
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
@RequestMapping("user/tasks")
@RequiredArgsConstructor
@Validated
public class TaskControllerUser {

    private final TaskService taskEmployeeService;
    private final TaskMapper taskMapper;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника с возможной фильтрацией по статусу задачи.
     */
    @Operation(
            summary = "Получение всех задач по ID сотрудника с возможной фильрацией по статусу задачи",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
    public List<TaskShortResponseDto> findAllTasksByEmployeeIdFilters(Principal principal,
                                                                      @RequestParam(required = false) String status) {
        return taskEmployeeService.findAllByExecutorIdFilters(status, principal).stream()
                .map(taskMapper::mapToShortDto).toList();
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
    public TaskFullResponseDto findTaskById(
            @Parameter(required = true) @PathVariable Long taskId,
            Principal principal) {
        return taskMapper.mapToFullDto(taskEmployeeService.findByIdAndExecutorId(principal, taskId));
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(
            summary = "Обновление статуса выполнения задачи сотрудником"
    )
    @PatchMapping("/{taskId}")
    public TaskFullResponseDto updateStatus(@Parameter(required = true) @PathVariable Long taskId,
                                            @Parameter(required = true) @RequestParam String status,
                                            Principal principal) {
        return taskMapper.mapToFullDto(taskEmployeeService.updateStatus(taskId, status, principal));

    }

    /**
     * Эндпоинт получения списка задач проекта с определенным статусом задач
     */
    @Operation(
            summary = "Получение списка задач проекта с определенным статусом задач",
            description = "При успешном получении возвращается 200 Ok\n" +
                    "В случае отсутствия проекта с указанным id возвращается 404 Not Found"
    )
    @GetMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskShortResponseDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                               @RequestParam TaskStatus status) {
        return taskEmployeeService.findByProjectIdAndStatus(projectId, status)
                .stream().map(taskMapper::mapToShortDto).toList();
    }
}