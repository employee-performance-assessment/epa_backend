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
import ru.epa.epabackend.dto.recommendation.RequestRecommendationDto;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.service.RecommendationService;

import java.security.Principal;
import java.util.List;

/**
 * Класс AdminRecommendationController содержит эндпойнты для администратора,
 * относящиеся к созданию рекомендаций для сотрудников.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Рекомендации", description = "API администратора для работы с рекомендациями")
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
    @Operation(summary = "Добавление новой рекомендации",
            description = "При успешном добавлении возвращается код 201 Created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRecommendationDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseRecommendationDto save(Principal principal, @RequestParam(required = true) String email,
                                          @RequestBody RequestRecommendationDto requestRecommendationDto) {
        Recommendation recommendation = recommendationService.create(requestRecommendationDto,
                email, principal.getName());
        return recommendationMapper.mapToDto(recommendation);
    }

    /**
     * Эндпойнт получение рекомендаций конкретного сотрудника.
     */
    @Operation(summary = "Получение рекомендаций руководителя для определенного сотрудника",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если не найдено ни одной рекомендации, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseRecommendationDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<ResponseRecommendationDto> findAllRecommendationByRecipientEmail(
            @RequestParam(required = true) String email) {
        return recommendationMapper.mapList(recommendationService.findAllByRecipientEmail(email));
    }

    /**
     * Эндпойнт получение всех рекомендаций.
     */
    @Operation(summary = "Получение всех рекомендаций",
            description = "Возвращает список рекомендаций" +
                    "\n\nВ случае, если не найдено ни одной рекомендации, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseRecommendationDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/all")
    public List<ResponseRecommendationDto> findAllRecommendations() {
        return recommendationMapper.mapList(recommendationService.findAll());
    }
}
