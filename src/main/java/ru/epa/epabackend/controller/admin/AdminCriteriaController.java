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
import ru.epa.epabackend.dto.evaluation.CriteriaDto;
import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.service.CriteriaService;

import java.util.List;

/**
 * Класс EvaluationControllerAdmin содержит эндпойнты для администратора, относящиеся к критериям оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Оценки", description = "API администратора для работы с критериями оценок")
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CriteriaDto save(@RequestBody CriteriaRequestDto criteriaRequestDto) {
        return criteriaMapper.mapToDto(criteriaService.create(criteriaRequestDto));
    }

    /**
     * Эндпойнт поиска всех оценок.
     */
    @Operation(
            summary = "Получение всех критериев оценок",
            description = "Возвращает список всех критериев оценок." +
                    "В случае, если не найдено ни одного критерия оценки, возвращает пустой список."
    )
    @GetMapping
    public List<CriteriaDto> findAll() {
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
    @GetMapping("/{criteriaId}")
    public CriteriaDto findById(@Parameter(required = true) @PathVariable Long criteriaId) {
        return criteriaMapper.mapToDto(criteriaService.findById(criteriaId));
    }

    /**
     * Эндпоинт получения дефолтных критериев (по умолчанию).
     */
    @Operation(summary = "Эндпоинт получения дефолтных критериев (по умолчанию).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CriteriaDto.class))))})
    @GetMapping("/default")
    public List<CriteriaDto> findDefault() {
        List<Criteria> criterias = criteriaService.findDefault();
        return criteriaMapper.mapList(criterias);
    }
}
