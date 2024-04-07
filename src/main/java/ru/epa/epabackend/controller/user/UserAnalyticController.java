package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.analytics.ResponseIndividualAnalyticsDto;
import ru.epa.epabackend.dto.analytics.ResponseTeamAnalyticsShortDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.AnalyticsMapper;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.service.AnalyticsService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;

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
public class UserAnalyticController {

    private final AnalyticsService analyticService;
    private final AnalyticsMapper analyticsMapper;

    /**
     * Эндпойнт получения статистики команды сотрудником за определенный период.
     */
    @Operation(summary = "Получения командной статистики сотрудником")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseTeamAnalyticsShortDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/team")
    public ResponseTeamAnalyticsShortDto findTeamStat(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate endDate,
            Principal principal) {
        TeamAnalytics stats = analyticService.getTeamStats(rangeStart, endDate, principal.getName());
        return analyticsMapper.mapToShortDto(stats);
    }

    /**
     * Эндпойнт получения индивидуальной статистики сотрудником за определенный период.
     */
    @Operation(summary = "Получения индивидуальной статистики сотрудником")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseIndividualAnalyticsDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/individual")
    public ResponseIndividualAnalyticsDto findIndividualStat(
            @RequestParam(name = "range-start") LocalDate rangeStart,
            @RequestParam(name = "range-end") LocalDate endDate,
            Principal principal) {
        IndividualAnalytics stat = analyticService.getIndividualStats(rangeStart, endDate, principal.getName());
        return analyticsMapper.mapToEntityIndividual(stat);
    }


    /**
     * Эндпойнт получения суммы баллов по выполненным задачам сотрудника за текущий месяц.
     */
    @Operation(summary = "Получение суммы баллов по выполненным задачам сотрудника за текущий месяц")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/points/employee/{employeeId}")
    public Integer findQuantityOfPointsForCurrentMonth(@Parameter(required = true) @PathVariable Long employeeId) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return analyticService.findQuantityOfPointsForCurrentMonth(employeeId, rangeStart, rangeEnd);
    }
}