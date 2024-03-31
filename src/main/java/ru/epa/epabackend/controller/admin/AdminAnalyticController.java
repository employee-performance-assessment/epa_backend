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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.analytics.ResponseIndividualAnalyticsDto;
import ru.epa.epabackend.dto.analytics.ResponseTeamAnalyticsFullDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.AnalyticsMapper;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.service.AnalyticsService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * Класс AnalyticControllerAdmin содержит эндпойнты для администратора, относящиеся к аналитике.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Аналитика", description = "Закрытый API для работы с аналитикой")
@RestController
@RequestMapping("/admin/stat")
@RequiredArgsConstructor
@Validated
public class AdminAnalyticController {

    private final AnalyticsService analyticService;
    private final AnalyticsMapper analyticsMapper;

    /**
     * Эндпойнт получения статистики команды администратором за определенный период.
     */
    @Operation(summary = "Получение командной статистики администратором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTeamAnalyticsFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/team")
    public ResponseTeamAnalyticsFullDto findTeamStatByAdmin(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate rangeEnd,
            Principal principal) {
        TeamAnalytics stats = analyticService.getTeamStatsByAdmin(rangeStart, rangeEnd, principal.getName());
        return analyticsMapper.mapToFullDto(stats);
    }

    /**
     * Эндпойнт получения индивидуальной статистики администратором за определенный период.
     */
    @Operation(summary = "Получения индивидуальной статистики администратором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseIndividualAnalyticsDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/individual")
    public List<ResponseIndividualAnalyticsDto> findIndividualStatByAdmin(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate rangeEnd,
            Principal principal) {
        List<IndividualAnalytics> stats = analyticService
                .getIndividualStatsByAdmin(rangeStart, rangeEnd, principal.getName());
        return analyticsMapper.mapList(stats);
    }
}