package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
<<<<<<<<< Temporary merge branch 1
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
=========
import ru.epa.epabackend.dto.task.TaskCreateFindByIdUpdateResponseDto;
import ru.epa.epabackend.dto.task.TaskFindAllResponseDto;
>>>>>>>>> Temporary merge branch 2
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

    /**
     * Эндпойнт поиска всех задач по ID сотрудника с возможной фильтрацией по статусу задачи.
     */
    @Operation(
            summary = "Получение всех задач по ID сотрудника с возможной фильрацией по статусу задачи",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
<<<<<<<<< Temporary merge branch 1
    public List<TaskShortDto> findAllTasksByEmployeeIdFilters(Principal principal,
                                                              @RequestParam(required = false) String status) {
=========
    public List<TaskFindAllResponseDto> findAllTasksByEmployeeIdFilters(Principal principal,
                                                                        @RequestParam(required = false) String status) {
>>>>>>>>> Temporary merge branch 2
        return taskEmployeeService.findAllByExecutorIdFilters(status, principal);
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
<<<<<<<<< Temporary merge branch 1
    public TaskFullDto findTaskById(@Parameter(required = true) @PathVariable Long taskId,
                                    Principal principal) {
=========
    public TaskCreateFindByIdUpdateResponseDto findTaskById(
            @Parameter(required = true) @PathVariable Long taskId,
                                                            Principal principal) {
>>>>>>>>> Temporary merge branch 2
        return taskEmployeeService.findByIdAndExecutorId(principal, taskId);
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(
            summary = "Обновление статуса выполнения задачи сотрудником"
    )
    @PatchMapping("/{taskId}")
    public TaskCreateFindByIdUpdateResponseDto updateStatus(@Parameter(required = true) @PathVariable Long taskId,
                                    @Parameter(required = true) @RequestParam String status,
                                    Principal principal) {
        return taskEmployeeService.updateStatus(taskId, status, principal);

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
    public List<TaskFindAllResponseDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                       @RequestParam TaskStatus status) {
        return taskEmployeeService.findByProjectIdAndStatus(projectId, status);
    }
}