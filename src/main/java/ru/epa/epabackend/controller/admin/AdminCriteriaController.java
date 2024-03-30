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
import ru.epa.epabackend.dto.criteria.CriteriaRequestDto;
import ru.epa.epabackend.dto.criteria.CriteriaResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
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
    @Operation(
            summary = "Добавление нового критерия оценки",
            description = "При успешном добавлении возвращается код 201 Created."
    )
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
    @ResponseStatus(HttpStatus.CREATED)
    public List<CriteriaResponseDto> save(@RequestBody List<CriteriaRequestDto> criteriaRequestDtoList) {
        return criteriaMapper.mapList(criteriaService.create(criteriaRequestDtoList));
    }

    /**
     * Эндпойнт поиска всех оценок.
     */
    @Operation(
            summary = "Получение всех критериев оценок",
            description = "Возвращает список всех критериев оценок." +
                    "В случае, если не найдено ни одного критерия оценки, возвращает пустой список."
    )
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
    public List<CriteriaResponseDto> findAll() {
        return criteriaMapper.mapList(criteriaService.findAll());
    }

    /**
     * Эндпойнт поиска названия оценки по её ID.
     */
    @Operation(
            summary = "Получение информации о названии критерия оценки",
            description = "Возвращает название критерия оценки и её ID, если она существует в базе данных. " +
                    "В случае, если критерия оценки не найдено, возвращает ошибку 404"
    )
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
    @GetMapping("/{criteriaId}")
    public CriteriaResponseDto findById(@Parameter(required = true) @PathVariable Long criteriaId) {
        return criteriaMapper.mapToDto(criteriaService.findById(criteriaId));
    }

    /**
     * Эндпоинт получения дефолтных критериев (по умолчанию).
     */
    @Operation(summary = "Эндпоинт получения дефолтных критериев (по умолчанию).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CriteriaResponseDto.class))))})
    @GetMapping("/default")
    public List<CriteriaResponseDto> findDefault() {
        List<Criteria> criterias = criteriaService.findDefault();
        return criteriaMapper.mapList(criterias);
    }
}
