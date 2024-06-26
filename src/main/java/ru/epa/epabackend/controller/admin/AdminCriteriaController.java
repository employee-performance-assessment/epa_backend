package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.service.CriteriaService;

import java.util.List;

/**
 * Класс содержит эндпойнты для админа, относящиеся к критериям оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Критерии оценок", description = "API админа для работы с критериями оценок")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/criteria")
public class AdminCriteriaController {

    private final CriteriaService criteriaService;
    private final CriteriaMapper criteriaMapper;

    /**
     * Получение дефолтных критериев (по умолчанию).
     */
    @Operation(summary = "Получение дефолтных критериев (по умолчанию).")
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
