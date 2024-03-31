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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.service.CriteriaService;

import java.util.List;

/**
 * Класс AdminCriteriaController содержит эндпойнты для администратора, относящиеся к критериям оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Критерии оценок", description = "API администратора для работы с критериями оценок")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/criteria")
public class AdminCriteriaController {

    private final CriteriaService criteriaService;
    private final CriteriaMapper criteriaMapper;

    /**
     * Эндпойнт добавления нового критерия оценки
     */
    @Operation(summary = "Добавление нового критерия оценки",
            description = "Возвращает список всех критериев оценок.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseCriteriaDto.class)))),
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
    public List<ResponseCriteriaDto> save(@RequestBody List<RequestCriteriaDto> requestCriteriaDtoList) {
        return criteriaMapper.mapList(criteriaService.create(requestCriteriaDtoList));
    }

    /**
     * Эндпойнт поиска всех оценок.
     */
    @Operation(summary = "Получение всех критериев оценок",
            description = "Возвращает список всех критериев оценок." +
                    "В случае, если не найдено ни одного критерия оценки, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseCriteriaDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<ResponseCriteriaDto> findAll() {
        return criteriaMapper.mapList(criteriaService.findAll());
    }

    /**
     * Эндпойнт поиска названия оценки по её ID.
     */
    @Operation(summary = "Получение информации о названии критерия оценки",
            description = "Возвращает название критерия оценки и её ID, если она существует в базе данных. " +
                    "В случае, если критерия оценки не найдено, возвращает ошибку 404")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseCriteriaDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{criteriaId}")
    public ResponseCriteriaDto findById(@Parameter(required = true) @PathVariable Long criteriaId) {
        return criteriaMapper.mapToDto(criteriaService.findById(criteriaId));
    }

    /**
     * Эндпойнт получения дефолтных критериев (по умолчанию).
     */
    @Operation(summary = "Эндпойнт получения дефолтных критериев (по умолчанию).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseCriteriaDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/default")
    public List<ResponseCriteriaDto> findDefault() {
        List<Criteria> criterias = criteriaService.findDefault();
        return criteriaMapper.mapList(criterias);
    }
}
