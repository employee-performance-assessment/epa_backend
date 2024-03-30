package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.service.EmployeeService;

import java.security.Principal;
import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс EmployeeControllerAdmin содержит эндпойнты для администратора, относящиеся к сотрудникам.
 *
 * @author Валентина Вахламова
 */
@Tag(name = "Admin: Сотрудники", description = "API для работы с пользователями")
@SecurityRequirement(name = "JWT")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/employee")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт добавления нового сотрудника
     */
    @Operation(summary = "Добавление нового сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = EmployeeFullResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeFullResponseDto addEmployee(@Validated(Create.class) @RequestBody @Parameter(required = true)
                                               EmployeeRequestDto employeeRequestDto,
                                               Principal principal) {
        return employeeMapper.mapToFullDto(employeeService.create(employeeRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт по обновлению существующего сотрудника
     */
    @Operation(summary = "Обновление сотрудника",
            description = "Обновляет данные сотрудника, если он существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = EmployeeFullResponseDto.class))),
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
    @PatchMapping("/{employeeId}")
    public EmployeeFullResponseDto updateEmployee(@PathVariable @Parameter(required = true) Long employeeId,
                                                  @Validated(Update.class) @RequestBody @Parameter(required = true)
                                                  EmployeeRequestDto employeeRequestDto) {
        return employeeMapper.mapToFullDto(employeeService.update(employeeId, employeeRequestDto));
    }

    /**
     * Эндпойнт удаления сотрудника
     */
    @Operation(summary = "Удаление сотрудника",
            description = "Удаляет данные сотрудника, если он существует в базе данных.")
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
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable @Parameter(required = true) Long employeeId) {
        employeeService.delete(employeeId);
    }

    /**
     * Эндпойнт получения всех сотрудников для одного админа
     */
    @Operation(summary = "Получение всех сотрудников для текущего админа",
            description = "Возвращает список сотрудников в сокращенном виде одной команды для одного админа\n\n" +
                    "В случае, если не найдено ни одного сотрудника, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = EmployeeShortResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<EmployeeShortResponseDto> findAllByAdmin(Principal principal) {
        List<Employee> employees = employeeService.findAllByCreatorEmail(principal.getName());
        return employeeMapper.mapList(employees);
    }
}