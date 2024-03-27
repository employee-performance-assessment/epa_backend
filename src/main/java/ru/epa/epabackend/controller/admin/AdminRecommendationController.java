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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.recommendation.RecommendationRequestDto;
import ru.epa.epabackend.dto.recommendation.RecommendationResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.service.RecommendationService;

import java.util.List;

/**
 * Класс AdminRecommendationController содержит эндпойнты для администратора,
 * относящиеся к созданию рекомендаций для сотрудников.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Рекомендации", description = "API администратора для работы рекомендациями")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/recommendation")
public class AdminRecommendationController {

    private final RecommendationService recommendationService;
    private final RecommendationMapper recommendationMapper;

    /**
     * Эндпойнт добавления новой рекомендации.
     */
    @Operation(
            summary = "Добавление новой рекомендации",
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
    @PostMapping("/{senderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RecommendationResponseDto save(@PathVariable("senderId") Long senderId,
                                          @RequestParam(required = true) Long recipientId,
            @RequestBody RecommendationRequestDto recommendationRequestDto) {
        Recommendation recommendation = recommendationService.create(recommendationRequestDto,
                recipientId, senderId);
        return recommendationMapper.mapToDto(recommendation);
    }

    /**
     * Эндпойнт получение рекомендаций конкретного сотрудника.
     */
    @Operation(
            summary = "Получение рекомендаций руководителя для определенного сотрудника",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если не найдено ни одной рекомендации, возвращает пустой список."
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
    public List<RecommendationResponseDto> findAllRecommendationByRecipientId(
            @RequestParam(required = true) Long recipientId) {
        return recommendationMapper.mapList(recommendationService.findAllByRecipientId(recipientId));
    }

    /**
     * Эндпойнт получение всех рекомендаций.
     */
    @Operation(
            summary = "Получение всех рекомендаций",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если не найдено ни одной рекомендации, возвращает пустой список."
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
    @GetMapping("/all")
    public List<RecommendationResponseDto> findAllRecommendations() {
        return recommendationMapper.mapList(recommendationService.findAll());
    }
}
