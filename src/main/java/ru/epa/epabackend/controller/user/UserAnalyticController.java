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
import java.util.List;

/**
 * Класс содержит эндпойнты для атворизованного пользователя, относящиеся к аналитике.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Private: Аналитика", description = "API пользователя для работы с аналитикой")
@RestController
@RequestMapping("user/stat")
@RequiredArgsConstructor
@Validated
public class UserAnalyticController {

    private final AnalyticsService analyticService;
    private final AnalyticsMapper analyticsMapper;

    /**
     * Получение командной статистики сотрудником за определенный период
     */
    @Operation(summary = "Получение командной статистики сотрудником за определенный период")
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
            @RequestParam Integer year,
            @RequestParam Integer month,
            Principal principal) {
        TeamAnalytics stats = analyticService.getTeamStats(year, month, principal.getName());
        return analyticsMapper.mapToShortDto(stats);
    }

    /**
     * Получение индивидуальной статистики сотрудником за определенный период
     */
    @Operation(summary = "Получение индивидуальной статистики сотрудником за определенный период")
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
            @RequestParam Integer year,
            @RequestParam Integer month,
            Principal principal) {
        IndividualAnalytics stat = analyticService.getIndividualStats(year, month, principal.getName());
        return analyticsMapper.mapToEntityIndividual(stat);
    }

    /**
     * Получение сотрудником суммы своих баллов по выполненным задачам за текущий месяц
     */
    @Operation(summary = "Получение сотрудником суммы своих баллов по выполненным задачам за текущий месяц")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/points")
    public Integer findQuantityOfPointsByUser(Principal principal) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return analyticService.findQuantityOfPointsByUser(principal, rangeStart, rangeEnd);
    }

    /**
     * Получение списка годов, в которые существует командная статистика по сотрудникам
     */
    @Operation(summary = "Получение списка годов, в который существует командная статистика по сотрудникам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = Integer.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/team/years")
    public List<Integer> findYearsForTeamStatistics(Principal principal) {
        return analyticService.findYearsForTeamStatistics(principal.getName());
    }

    /**
     * Получение списка месяцев, в которые существует командная статистика по сотрудникам в определенный год
     */
    @Operation(summary = "Получение списка месяцев, в которые существует командная статистика по сотрудникам " +
            "в определенный год")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = Integer.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/team/{year}/months")
    public List<Integer> findMonthsForTeamStatistics(@PathVariable Integer year, Principal principal) {
        return analyticService.findMonthsForTeamStatistics(year, principal.getName());
    }

    /**
     * Получение списка годов, в которые существует индивидуальная статистика по сотруднику
     */
    @Operation(summary = "Получение списка годов, в которые существует индивидуальная статистика по сотруднику")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = Integer.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/individual/years")
    public List<Integer> findYearsForIndividualStatistics(Principal principal) {
        return analyticService.findYearsForIndividualStatistics(principal.getName());
    }

    /**
     * Получение списка месяцев, в которые существует индивидуальная статистика по сотруднику в определенный год
     */
    @Operation(summary = "Получение списка месяцев, в которые существует индивидуальная статистика по сотруднику " +
            "в определенный год")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = Integer.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/task/individual/{year}/months")
    public List<Integer> findMonthsForIndividualStatistics(@PathVariable Integer year, Principal principal) {
        return analyticService.findMonthsForIndividualStatistics(year, principal.getName());
    }
}