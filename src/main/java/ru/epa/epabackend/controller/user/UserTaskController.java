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
import ru.epa.epabackend.dto.task.ResponseTaskFullDto;
import ru.epa.epabackend.dto.task.ResponseTaskShortDto;
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
public class UserTaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Эндпойнт поиска всех задач по email сотрудника с возможной фильтрацией по статусу задачи.
     */
    @Operation(summary = "Получение всех задач по email сотрудника с возможной фильрацией по статусу задачи",
            description = "Возвращает список задач в сокращенном виде в случае, " +
                    "если не найдено ни одной задачи, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseTaskShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<ResponseTaskShortDto> findAllByEmployeeEmailAndStatus(Principal principal,
                                                                      @RequestParam(required = false) String status) {
        List<Task> allByExecutor;
        if (status == null) {
            allByExecutor = taskService.findAllByExecutorEmail(principal);
        } else {
            allByExecutor = taskService.findAllByExecutorEmailAndStatus(status, principal);
        }
        return taskMapper.mapList(allByExecutor);
    }

    /**
     * Эндпойнт поиска задачи по email сотрудника и ID задачи.
     */
    @Operation(summary = "Получение информации о задаче сотрудником",
            description = "Возвращает полную информацию о задаче, если она существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTaskFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{taskId}")
    public ResponseTaskFullDto findByIdAndExecutorEmail(
            @Parameter(required = true) @PathVariable Long taskId,
            Principal principal) {
        return taskMapper.mapToFullDto(taskService.findByIdAndExecutorEmail(principal, taskId));
    }

    /**
     * Эндпойнт обновления статуса задачи.
     */
    @Operation(summary = "Обновление статуса выполнения задачи сотрудником",
            description = "Возможные статусы задач :" +
            " Новая задача NEW, Задача над которой ведется работа IN_PROGRESS," +
            " Задача на проверке у руководителя REVIEW , Задача выполнена DONE," +
            " Задача отменена или заморожена на неопределенный срок CANCELED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTaskFullDto.class))),
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
    public ResponseTaskFullDto updateStatus(@Parameter(required = true) @PathVariable Long taskId,
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
                    schema = @Schema(implementation = ResponseTaskShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/project/{projectId}")
    public List<ResponseTaskShortDto> findByProjectIdAndStatus(@PathVariable Long projectId,
                                                               @RequestParam TaskStatus status) {
        List<Task> byProjectIdAndStatus = taskService.findByProjectIdAndStatus(projectId, status);
        return taskMapper.mapList(byProjectIdAndStatus);
    }

    /**
     * Эндпойнт поиска всех задач сотрудником для канбан доски. Видим все задачи всех сотрудников администратора.
     */
    @Operation(summary = "Эндпойнт поиска всех задач сотрудником для канбан доски. " +
            "Видим все задачи всех сотрудников администратора.",
            description = "Возвращает список задач в сокращенном виде. " +
                    "В случае, если не найдено ни одной задачи, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseTaskShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/all-employees")
    public List<ResponseTaskShortDto> findAllForEmployee(Principal principal, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return taskMapper.mapList(taskService.findAllForEmployeeByProjectId(principal.getName(), projectId));
        } else {
            return taskMapper.mapList(taskService.findAllForEmployee(principal.getName()));
        }
    }
}