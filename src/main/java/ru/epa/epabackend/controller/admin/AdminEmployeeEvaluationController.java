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
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponsePersonalRatingDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingFullDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.security.Principal;
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
    @Operation(summary = "Получение рейтинга сотрудника с учетом оценок коллег",
            description = "Возвращает рейтинг сотрудника с учетом оценок коллег" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает 0.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/full")
    public ResponseRatingDto findFullRating(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) LocalDate startDay,
            @RequestParam(required = true) LocalDate endDay) {
        return employeeEvaluationService.findFullRating(email, startDay, endDay);
    }

    /**
     * Эндпойнт получения рейтинга сотрудника без учета оценок коллег.
     */
    @Operation(summary = "Получение рейтинга сотрудника без учета оценок коллег",
            description = "Возвращает рейтинг сотрудника без учета оценок коллег" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает 0.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating")
    public ResponseRatingDto findRatingByAdmin(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) LocalDate startDay,
            @RequestParam(required = true) LocalDate endDay) {
        return employeeEvaluationService.findRatingByAdmin(email, startDay, endDay);
    }

    /**
     * Эндпойнт получения оценок коллег о сотруднике.
     */
    @Operation(summary = "Получение оценок коллег о сотруднике",
            description = "Возвращает список оценок коллег (усредненный по оценкам)" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeEvaluationDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/evaluations/users")
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsByEvaluatedEmail(
            @RequestParam(required = true) String email) {
        return employeeEvaluationService.findAllEvaluationsUsers(email);
    }

    /**
     * Эндпойнт получения всех оценок руководителя о сотруднике.
     */
    @Operation(summary = "Получение оценок руководителя для сотрудника",
            description = "Возвращает список оценок руководителя о сотруднике" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeEvaluationDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/evaluations")
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdminByEvaluatedEmail(
            @RequestParam(required = true) String email) {
        return employeeEvaluationService.findAllEvaluationsAdmin(email);
    }

    /**
     * Эндпойнт получения командного рейтинга.
     */
    @Operation(summary = "Получение командного рейтинга",
            description = "Возвращает список рейтинга команды за каждый оцененный месяц" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/command")
    public List<ResponseRatingFullDto> findCommandRating(Principal principal) {
        return employeeEvaluationService.findCommandRatingForAdmin(principal.getName());
    }

    /**
     * Эндпойнт получения персонального рейтинга каждого сотрудника за каждый месяц.
     */
    @Operation(summary = "получения персонального рейтинга каждого сотрудника за каждый месяц",
            description = "Возвращает список личных рейтингов каждого сотрудника" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/personal")
    public List<ResponsePersonalRatingDto> findPersonalRating(Principal principal) {
        return employeeEvaluationService.findPersonalRatingAdmin(principal.getName());
    }
}
