package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.service.TaskService;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Класс TaskControllerUser содержит эндпойнты для атворизованного пользователя, относящиеся к задачам.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Private: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("user/task")
@RequiredArgsConstructor
@Validated
public class TaskControllerUser {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Эндпойнт поиска всех задач по ID сотрудника с возможной фильтрацией по статусу задачи.
     */
    @Operation(summary = "Получение всех задач по ID сотрудника с возможной фильрацией по статусу задачи",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = TaskShortResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<TaskShortResponseDto> findAllByEmployeeIdFilters(Principal principal,
                                                                 @RequestParam(required = false) String status) {
        List<Task> allByExecutorIdFilters = taskService.findAllByExecutorIdFilters(status, principal);
        return taskMapper.mapList(allByExecutorIdFilters);
    }

    /**
     * Эндпойнт поиска задачи по ID сотрудника и ID задачи.
     */
    @Operation(summary = "Получение информации о задаче сотрудником",
            description = "Возвращает полную информацию о задаче, если она существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = TaskFullResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{taskId}")
    public TaskFullResponseDto findById(
            @Parameter(required = true) @PathVariable Long taskId,
            Principal principal) {
        return taskMapper.mapToFullDto(taskService.findByIdAndExecutorId(principal, taskId));
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(summary = "Обновление статуса выполнения задачи сотрудником")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = TaskFullResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskFullResponseDto updateStatus(@Parameter(required = true) @PathVariable Long taskId,
                                            @Parameter(required = true) @RequestParam String status,
                                            Principal principal) {
        return taskMapper.mapToFullDto(taskService.updateStatus(taskId, status, principal));

    }

    /**
     * Эндпоинт получения списка задач проекта с определенным статусом задач
     */
    @Operation(summary = "Получение списка задач проекта с определенным статусом задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = TaskShortResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/project/{projectId}")
    public List<TaskShortResponseDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                               @RequestParam TaskStatus status) {
        List<Task> byProjectIdAndStatus = taskService.findByProjectIdAndStatus(projectId, status);
        return taskMapper.mapList(byProjectIdAndStatus);
    }
}