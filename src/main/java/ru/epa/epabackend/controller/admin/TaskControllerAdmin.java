package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.service.TaskService;

import java.security.Principal;
import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс TaskControllerAdmin содержит эндпойнты для администратора, относящиеся к задачам.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("/admin/task")
@RequiredArgsConstructor
@Validated
public class TaskControllerAdmin {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Эндпойнт поиска всех задач администратором.
     */
    @Operation(summary = "Получение всех задач администратором",
            description = "Возвращает список задач в сокращенном виде. " +
                    "В случае, если не найдено ни одной задачи, возвращает пустой список.")
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
    public List<TaskShortResponseDto> findAll(Principal principal) {
        return taskMapper.mapList(taskService.findAll(principal.getName()));
    }

    /**
     * Эндпойнт поиска задачи по ID администратором.
     */
    @Operation(summary = "Получение информации о задаче администратором",
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
    public TaskFullResponseDto findById(@Parameter(required = true) @PathVariable Long taskId,
                                        Principal principal) {
        return taskMapper.mapToFullDto(taskService.findDtoById(taskId, principal.getName()));
    }

    /**
     * Эндпойнт поиска задачи по ID администратором.
     */
    @Operation(summary = "Получение информации о задаче администратором",
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
    @GetMapping("/find")
    public List<TaskShortResponseDto> findAllByEmployeeId(@Valid @RequestParam @Positive Long employeeId,
                                                          Principal principal) {
        List<Task> tasks = taskService.findAllByEmployeeId(employeeId, principal.getName());
        return taskMapper.mapList(tasks);
    }

    /**
     * Эндпойнт создания задачи.
     */
    @Operation(summary = "Добавление новой задачи администратором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = TaskFullResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping()
    public TaskFullResponseDto create(@Validated(Create.class) @Parameter(required = true)
                                      @RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.create(taskRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт обновления задачи.
     */
    @Operation(summary = "Обновление задачи администратором")
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
    public TaskFullResponseDto update(@Parameter(required = true) @PathVariable Long taskId,
                                      @Validated(Update.class) @Parameter(required = true)
                                      @RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.update(taskId, taskRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт удаления задачи.
     */
    @Operation(summary = "Удаление задачи администратором", description = "Удаляет задачу, если она существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/{taskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@Parameter(required = true) @PathVariable Long taskId, Principal principal) {
        taskService.delete(taskId, principal.getName());
    }
}