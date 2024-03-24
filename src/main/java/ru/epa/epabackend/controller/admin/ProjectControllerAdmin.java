package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectSaveWithEmployeeResponseDto;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.service.ProjectService;
import ru.epa.epabackend.util.Role;

import java.security.Principal;
import java.util.List;

/**
 * Класс ProjectControllerAdmin содержит эндпойнты для администратора, относящиеся к проектам.
 *
 * @author Константин Осипов
 */
@Tag(name = "Admin: Проекты", description = "API администратора для работы с проектами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/project")
public class ProjectControllerAdmin {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт добавления нового проекта
     */
    @Operation(summary = "Добавление нового проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ProjectShortResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectShortResponseDto save(@Valid @RequestBody ProjectCreateRequestDto projectCreateRequestDto,
                                        Principal principal) {
        return projectMapper.mapToShortDto(projectService.create(projectCreateRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт добавления сотрудника в проект
     */
    @Operation(
            summary = "Добавление сотрудника в проект",
            description = "Добавляет сотрудника в проект, если сотрудник и проект существуют.\n" +
                    "При успешном добавлении возвращается код 201 Created.\n " +
                    "В случае отсутствия сотрудника или проекта возвращается ошибка 404 Not Found.\n" +
                    "Когда проект не относится к администратору получаем 409 Conflict.\n" +
                    "Если сотрудник был добавлен в проект ранее, возвращается ошибка 409 Conflict."
    )
    @PostMapping("/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectSaveWithEmployeeResponseDto saveWithEmployee(@PathVariable Long projectId,
                                                               @RequestParam Long employeeId,
                                                               Principal principal) {
        Project project = projectService.saveWithEmployee(projectId, employeeId, principal.getName());
        return projectMapper.mapToProjectEmployeesDto(project, employeeMapper.mapList(project.getEmployees()));
    }

    /**
     * Эндпойнт получения списка сотрудников, участвующих в проекте
     */
    @Operation(
            summary = "Получение списка сотрудников, участвующих в проекте",
            description = "!!!Не предусмотрено текущим дизайном. " +
                    "При успешном получении списка сотрудников проекта возвращается 200 Ok.\n" +
                    "В случае отсутствия проекта или email'а администратора возвращается 404 Not Found.\n" +
                    "Когда проект не относится к администратору получаем 409 Conflict."
    )
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeShortResponseDto> findByProjectIdAndRole(@PathVariable Long projectId, Principal principal) {
        List<Employee> allByProjectIdAndRole = projectService.findAllByProjectIdAndRole(projectId, Role.ROLE_USER,
                principal.getName());
        return employeeMapper.mapList(allByProjectIdAndRole);
    }

    /**
     * Эндпойнт изменения информации о проекте
     */
    @Operation(summary = "Изменение информации о проекте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ProjectShortResponseDto.class))),
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
    @PatchMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectShortResponseDto update(@PathVariable Long projectId,
                                          @Valid @RequestBody ProjectUpdateRequestDto projectUpdateRequestDto,
                                          Principal principal) {
        return projectMapper.mapToShortDto(projectService.update(projectId, projectUpdateRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт удаления проекта
     */
    @Operation(summary = "Удаление проекта")
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
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long projectId, Principal principal) {
        projectService.delete(projectId, principal.getName());
    }

    /**
     * Эндпойнт удаления сотрудника из проекта
     */
    @Operation(
            summary = "Удаление сотрудника из проекта",
            description = "!!!Не предусмотрено текущим дизайном. " +
                    "При успешном удалении - 204 No Content\n" +
                    "Если сотрудника или проекта с указанными id не существует возвращается 404 Not Found.\n" +
                    "В случае отсутствия проекта с указанным id или отсутствия email администратора в базе данных " +
                    "вернётся ошибка 404 Not Found.\n" +
                    "Когда проект не относится к администратору получаем 409 Conflict.\n" +
                    "В случае, если сотрудник не состоял в проекте возвращается 409 Conflict."
    )
    @DeleteMapping("/{projectId}/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeFromProject(@PathVariable Long projectId,
                                          @PathVariable Long employeeId,
                                          Principal principal) {
        projectService.deleteEmployeeFromProject(projectId, employeeId, principal.getName());
    }
}