package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.epa.epabackend.dto.analytics.IndividualAnalyticsResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsShortResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.AnalyticsIndividualMapper;
import ru.epa.epabackend.mapper.AnalyticsTeamMapper;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.service.AnalyticsService;

import java.security.Principal;
import java.time.LocalDate;

/**
 * Класс AnalyticControllerUser содержит эндпойнты для атворизованного пользователя, относящиеся к аналитике.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Private: Аналатика", description = "Закрытый API для работы с аналитикой")
@RestController
@RequestMapping("user/stat")
@RequiredArgsConstructor
@Validated
public class AnalyticControllerUser {

    private final AnalyticsService analyticService;
    private final AnalyticsIndividualMapper analyticsIndividualMapper;
    private final AnalyticsTeamMapper analyticsTeamMapper;

    /**
     * Эндпойнт получения статистики команды сотрудником за определенный период.
     */
    @Operation(summary = "Получения командной статистики сотрудником")
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
    @GetMapping("/task/team")
    public TeamAnalyticsShortResponseDto findTeamStat(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate endDate,
            Principal principal) {
        TeamAnalytics stats = analyticService.getTeamStats(rangeStart, endDate, principal.getName());
        return analyticsTeamMapper.mapToShortDto(stats);
    }

    /**
     * Эндпойнт получения индивидуальной статистики сотрудником за определенный период.
     */
    @Operation(summary = "Получения индивидуальной статистики сотрудником")
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
    @GetMapping("/task/individual")
    public IndividualAnalyticsResponseDto findIndividualStat(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate endDate,
            Principal principal) {
        IndividualAnalytics stat = analyticService.getIndividualStats(rangeStart, endDate, principal.getName());
        return analyticsIndividualMapper.mapToEntityIndividual(stat);
    }
}