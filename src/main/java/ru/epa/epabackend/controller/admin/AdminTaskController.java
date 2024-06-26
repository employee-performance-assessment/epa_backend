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
import ru.epa.epabackend.dto.task.RequestTaskDto;
import ru.epa.epabackend.dto.task.ResponseTaskFullDto;
import ru.epa.epabackend.dto.task.ResponseTaskShortDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.service.TaskService;

import java.security.Principal;
import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс содержит эндпойнты для админа, относящиеся к задачам.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Задачи", description = "API админа для работы с задачами")
@RestController
@RequestMapping("/admin/task")
@RequiredArgsConstructor
@Validated
public class AdminTaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Получение всех задач админом
     */
    @Operation(summary = "Получение всех задач админом",
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
    @GetMapping
    public List<ResponseTaskShortDto> findAll(Principal principal, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return taskMapper.mapList(taskService.findAllByProjectId(principal.getName(), projectId));
        } else {
            return taskMapper.mapList(taskService.findAll(principal.getName()));
        }
    }

    /**
     * Получение информации о задаче админом
     */
    @Operation(summary = "Получение информации о задаче админом",
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
    public ResponseTaskFullDto findById(@Parameter(required = true) @PathVariable Long taskId,
                                        Principal principal) {
        return taskMapper.mapToFullDto(taskService.findDtoById(taskId, principal.getName()));
    }

    /**
     * Получение информации о всех задачах сотрудника админом с возможным поиском по названию или описанию задачи
     */
    @Operation(summary = "Получение информации о всех задачах сотрудника админом с возможным поиском по " +
            "названию или описанию задачи",
            description = "Возвращает полную информацию о всех задачах сотрудника, если она существует в базе данных.")
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
    @GetMapping("/find")
    public List<ResponseTaskShortDto> findAllByEmployeeId(@Valid @RequestParam @Positive Long employeeId,
                                                          Principal principal,
                                                          @RequestParam(required = false) String status,
                                                          @RequestParam(required = false) String text) {

        List<Task> allByEmployeeId;
        if (status == null) {
            allByEmployeeId = taskService.findAllByEmployeeId(employeeId, principal.getName(), text);
        } else {
            allByEmployeeId = taskService.findAllByEmployeeIdAndStatus(employeeId, principal.getName(), status, text);
        }
        return taskMapper.mapList(allByEmployeeId);
    }

    /**
     * Добавление новой задачи админом
     */
    @Operation(summary = "Добавление новой задачи админом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTaskFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseTaskFullDto create(@Validated(Create.class) @Parameter(required = true)
                                      @RequestBody RequestTaskDto requestTaskDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.create(requestTaskDto, principal.getName()));
    }

    /**
     * Обновление задачи админом
     */
    @Operation(summary = "Обновление задачи админом",
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
    public ResponseTaskFullDto update(@Parameter(required = true) @PathVariable Long taskId,
                                      @Validated(Update.class) @Parameter(required = true)
                                      @RequestBody RequestTaskDto requestTaskDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.update(taskId, requestTaskDto, principal.getName()));
    }

    /**
     * Удаление задачи админом
     */
    @Operation(summary = "Удаление задачи админом", description = "Удаляет задачу, если она существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/{taskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@Parameter(required = true) @PathVariable Long taskId, Principal principal) {
        taskService.delete(taskId, principal.getName());
    }
}