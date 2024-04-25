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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.technology.RequestTechnologyDto;
import ru.epa.epabackend.dto.technology.ResponseTechnologyDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс содержит эндпойнты для админа, относящиеся к технологиям.
 *
 * @author Артем Масалкин
 */

@Validated
@RestController
@RequestMapping("/admin/technology")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Технологии", description = "API админа для работы с технологиями")
public class AdminTechnologyController {

    private final TechnologyService technologyService;
    private final TechnologyMapper technologyMapper;

    /**
     * Добавление технологии
     */
    @Operation(summary = "Добавление технологии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTechnologyDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseTechnologyDto createTechnology(@Validated(Create.class) @Parameter(required = true)
                                                  @RequestBody RequestTechnologyDto technologyDto) {
        return technologyMapper.mapToDto(technologyService.create(technologyDto));
    }

    /**
     * Обновление технологии
     */
    @Operation(summary = "Обновление технологии", description = "Обновляет технологию, если она существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTechnologyDto.class))),
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
    @PatchMapping("/{technologyId}")
    public ResponseTechnologyDto updateTechnology(@Validated(Update.class) @Parameter(required = true)
                                                  @RequestBody RequestTechnologyDto technologyDto,
                                                  @PathVariable("technologyId") Long technologyId) {
        return technologyMapper.mapToDto(technologyService.update(technologyDto, technologyId));
    }

    /**
     * Получение технологии по id
     */
    @Operation(summary = "Получение технологии по id",
            description = "Возвращает полную информацию о технологии по id, если он существует в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTechnologyDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{technologyId}")
    public ResponseTechnologyDto findByIdDto(@PathVariable @Parameter(required = true) Long technologyId) {
        return technologyMapper.mapToDto(technologyService.findById(technologyId));
    }

    /**
     * Получение списка всех технологий
     */
    @Operation(summary = "Получение списка всех технологий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = ResponseTechnologyDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<ResponseTechnologyDto> getAllTechnologies() {
        List<Technology> technologies = technologyService.findAll();
        return technologyMapper.mapList(technologies);
    }

    /**
     * Удаление технологии
     */
    @Operation(summary = "Удаление технологии", description = "Удаляет технологию, если она существует в базе данных.")
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
    @DeleteMapping("/{technologyId}")
    public void deleteTechnologyById(@PathVariable Long technologyId) {
        technologyService.delete(technologyId);
    }
}
