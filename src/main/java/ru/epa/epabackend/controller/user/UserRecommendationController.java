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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.recommendation.RecommendationResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.service.RecommendationService;

import java.util.List;

/**
 * Класс UserRecommendationController содержит эндпойнты для сотрудников,
 * относящиеся к просмотру рекомендаций руководителя.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Private: Рекомендации", description = "Закрытый API для работы рекомендациями руководителя")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/recommendation")
public class UserRecommendationController {

    private final RecommendationService recommendationService;
    private final RecommendationMapper recommendationMapper;

    /**
     * Эндпойнт получение рекомендаций от руководителя.
     */
    @Operation(summary = "Получение всех рекомендаций руководителя для определенного сотрудника",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если не найдено ни одной рекомендации, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = RecommendationResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{recipientId}")
    public List<RecommendationResponseDto> findAllRecommendation(@PathVariable("recipientEmail") String recipientEmail) {
        List<Recommendation> recommendationList = recommendationService.findAllByRecipientEmail(recipientEmail);
        return recommendationMapper.mapList(recommendationList);
    }

    /**
     * Эндпойнт получение рекомендаций от руководителя по её ID.
     */
    @Operation(summary = "Получение рекомендации руководителя по ё ID",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если рекомендация не найдена, возвращает null.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RecommendationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public RecommendationResponseDto findRecommendationById(
            @RequestParam(required = true) Long recommendationId) {
        return recommendationMapper.mapToDto(recommendationService.findById(recommendationId));
    }
}
