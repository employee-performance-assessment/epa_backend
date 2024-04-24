package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.epa.epabackend.dto.project.ResponseProjectShortDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.service.ProjectService;

import java.security.Principal;
import java.util.List;

/**
 * Класс содержит эндпойнты для атворизованного пользователя, относящиеся к проектам.
 *
 * @author Константин Осипов
 */
@Tag(name = "Private: Проекты", description = "API пользователя для работы с проектами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/project")
public class UserProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    /**
     * Получение короткой информации о проекте
     */
    @Operation(summary = "Получение короткой информации о проекте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseProjectShortDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseProjectShortDto findProject(@PathVariable Long projectId, Principal principal) {
        return projectMapper.mapToShortDto(projectService.findDtoById(projectId, principal.getName()));
    }

    /**
     * Получение списка проектов админа с короткой информацией о проектах.
     * Пользователи закреплены за определенным админом и также могут видеть все его проекты
     */
    @Operation(summary = "Получение списка проектов админа с короткой информацией о проектах",
            description = "Пользователи закреплены за определенным админом и также могут видеть все его проекты.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = ResponseProjectShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseProjectShortDto> findAllByCreator(Principal principal) {
        List<Project> allByUserEmail = projectService.findAllByCreator(principal.getName());
        return projectMapper.mapAsList(allByUserEmail);
    }
}
