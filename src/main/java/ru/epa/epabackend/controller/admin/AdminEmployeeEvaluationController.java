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
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationResponseDto;
import ru.epa.epabackend.dto.evaluation.RatingResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс AdminEmployeeEvaluationController содержит эндпойнты для администратора,
 * относящиеся к просмотру оценок и рейтинга сотрудников.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Оценки и рейтинг",
        description = "API администратора для получения оценок и рейтинга сотрудников")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEmployeeEvaluationController {

    private final EmployeeEvaluationService employeeEvaluationService;

    /**
     * Эндпойнт получения рейтинга сотрудника с учетом оценок коллег.
     */
    @Operation(
            summary = "Получение рейтинга сотрудника с учетом оценок коллег",
            description = "Возвращает рейтинг сотрудника с учетом оценок коллег" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает 0."
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
    @GetMapping("/rating/full")
    public RatingResponseDto findFullRating(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) LocalDate startDay,
            @RequestParam(required = true) LocalDate endDay) {
        return employeeEvaluationService.findFullRating(email, startDay, endDay);
    }

    /**
     * Эндпойнт получения рейтинга сотрудника без учета оценок коллег.
     */
    @Operation(
            summary = "Получение рейтинга сотрудника без учета оценок коллег",
            description = "Возвращает рейтинг сотрудника без учета оценок коллег" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает 0."
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
    @GetMapping("/rating")
    public RatingResponseDto findRatingByAdmin(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) LocalDate startDay,
            @RequestParam(required = true) LocalDate endDay) {
        return employeeEvaluationService.findRatingByAdmin(email, startDay, endDay);
    }

    /**
     * Эндпойнт получения оценок коллег о сотруднике.
     */
    @Operation(
            summary = "Получение оценок коллег о сотруднике",
            description = "Возвращает список оценок коллег (усредненный по оценкам)" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
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
    @GetMapping("/evaluations/users")
    public List<EmployeeEvaluationResponseDto> findAllEvaluationsByEvaluatedEmail(
            @RequestParam(required = true) String email) {
        return employeeEvaluationService.findAllEvaluationsUsers(email);
    }

    /**
     * Эндпойнт получения сотрудником всех оценок руководителя о себе.
     */
    @Operation(
            summary = "Получение оценок руководителя для сотрудника",
            description = "Возвращает список оценок руководителя о сотруднике" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
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
    @GetMapping("/evaluations")
    public List<EmployeeEvaluationResponseDto> findAllEvaluationsAdminByEvaluatedEmail(
            @RequestParam(required = true) String email) {
        return employeeEvaluationService.findAllEvaluationsAdmin(email);
    }
}
