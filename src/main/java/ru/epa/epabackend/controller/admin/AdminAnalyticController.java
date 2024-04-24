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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.analytics.ResponseIndividualAnalyticsDto;
import ru.epa.epabackend.dto.analytics.ResponseTeamAnalyticsFullDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.AnalyticsMapper;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.service.AnalyticsService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Класс содержит эндпойнты для админа, относящиеся к аналитике.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Аналитика", description = "API админа для работы с аналитикой")
@RestController
@RequestMapping("/admin/stat")
@RequiredArgsConstructor
@Validated
public class AdminAnalyticController {

    private final AnalyticsService analyticService;
    private final AnalyticsMapper analyticsMapper;

    /**
     * Получение командной статистики админом за определенный период.
     */
    @Operation(summary = "Получение командной статистики админом за определенный период.")
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
     * Получения индивидуальной статистики админом за определенный период.
     */
    @Operation(summary = "Получения индивидуальной статистики админом за определенный период.")
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

    /**
     * Получение админом суммы баллов сотрудника по выполненным задачам сотрудника за текущий месяц
     */
    @Operation(summary = "Получение админом суммы баллов сотрудника по выполненным задачам сотрудника за " +
            "текущий месяц")
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
    public Integer findQuantityOfPointsByAdmin(@Parameter(required = true) @PathVariable Long employeeId,
                                               Principal principal) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return analyticService.findQuantityOfPointsByAdmin(employeeId, rangeStart, rangeEnd, principal.getName());
    }
}