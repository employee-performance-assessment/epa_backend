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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.RecommendationService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
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
    private final RecommendationService recommendationService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;

    /**
     * Эндпойнт получения руководителем персонального рейтинга сотрудника за каждый месяц указанного года.
     */
    @Operation(summary = "получения руководителем персонального рейтинга сотрудника за каждый месяц указанного года",
            description = "Возвращает список месяцев и среднюю оценку по ним " +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponsePersonalRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/personal")
    public List<ResponseRatingFullDto> findPersonalRating(Principal principal, @RequestParam Long evaluatedId,
                                                          @RequestParam Integer year) {
        return employeeEvaluationService.findPersonalRatingAdmin(principal.getName(), evaluatedId, year);
    }

    /**
     * Эндпойнт получения списка оцененных сотрудников.
     */
    @Operation(
            summary = "Получение сотрудником списка оцененных коллег",
            description = "Возвращает список оцененных коллег" +
                    "\n\nВ случае, если не найдено ни одного оцененного сотрудника, возвращает пустой список."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/list-evaluated")
    public List<ResponseEmployeeShortDto> findAllRated(Principal principal) {
        List<Employee> employees = employeeEvaluationService.findAllRated(principal.getName());
        return employeeMapper.mapList(employees);
    }

    /**
     * Эндпойнт добавления оценок руководителя
     */
    @Operation(summary = "Сохранения оценок сотрудника своего коллеги",
            description = "При успешном добавлении возвращается код 201 Created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseAdminEvaluationDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/evaluations")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseAdminEvaluationDto save(
            Principal principal, @RequestParam Long questionnaireId,
            @RequestParam Long evaluatedId,
            @Valid @RequestBody RequestAdminEvaluationDto evaluationsAdmin) {
        List<EmployeeEvaluation> employeeEvaluations = employeeEvaluationService.create(principal.getName(),
                evaluatedId, questionnaireId, evaluationsAdmin.getEvaluationDtoList());
        Recommendation recommendation = recommendationService.create(evaluationsAdmin.getRecommendation(),
                questionnaireId, evaluatedId, principal.getName());
        return ResponseAdminEvaluationDto
                .builder()
                .adminEvaluations(employeeEvaluationMapper.mapToShortListDto(employeeEvaluations))
                .recommendation(recommendation.getRecommendation())
                .build();
    }

    /**
     * Эндпойнт получения руководителя всех оценок и рекомендации по ID анкеты и ID сотрудника.
     */
    @Operation(
            summary = "Получение сотрудником всех своих оценок и рекомендации",
            description = "Возвращает список всех оценок коллег (усредненный по оценкам)" +
                    "оценки руководителя и рекомендацию руководителя" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeEvaluationQuestionnaireDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/evaluations")
    public ResponseEmployeeEvaluationQuestionnaireDto findEvaluationsAndRecommendation(
            Principal principal, @RequestParam Long questionnaireId, @RequestParam Long evaluatedId) {
        return employeeEvaluationService.findAllEvaluationsByQuestionnaireIdForAdmin(principal.getName(),
                questionnaireId, evaluatedId);
    }

    /**
     * Эндпойнт получения списка анкет в которых оценен сотрудник.
     */
    @Operation(
            summary = "Получение админом списка анкет в которых оценен сотрудник",
            description = "Возвращает список анкет в которых оценен сотрудник" +
                    "\n\nВ случае, если не найдено ни одной анкеты, возвращает пустой список."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseEvaluatedQuestionnaireDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/list-questionnaire")
    public List<ResponseEvaluatedQuestionnaireDto> findListQuestionnaireByEvaluatedId(
            Principal principal,
            @RequestParam Long evaluatedId,
            @RequestParam(required = false) @Min(0) @Max(5) Integer stars,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        List<ResponseEvaluatedQuestionnaireDto> listQuestionnaire = employeeEvaluationService
                .findAllQuestionnaireByEvaluatedId(principal.getName(), evaluatedId, stars, from, to);
        return listQuestionnaire;
    }

    /**
     * Эндпойнт получения руководителем оценок и рекомендации по ID анкеты и ID сотрудника в разделе Оценка ЭС.
     */
    @Operation(
            summary = "Эндпойнт получения руководителем всех оценок и рекомендации по ID анкеты и ID сотрудника " +
                    "в разделе Оценка ЭС ",
            description = "Возвращает список оценок и рекомендацию, оставленные руководителем по определенной анкете " +
                    "для указанного сотрудника."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseAdminEvaluationDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/assessed")
    public ResponseAdminEvaluationDto findAssessedQuestionnaire(
            Principal principal, @RequestParam Long questionnaireId, @RequestParam Long evaluatedId) {
        return employeeEvaluationService.findAssessedQuestionnaireByAdmin(principal.getName(), questionnaireId, evaluatedId);
    }

    /**
     * Эндпойнт получения администратором среднего рейтинга за текущий месяц.
     */
    @Operation(summary = "Получение администратором среднего рейтинга за текущий месяц")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/{employeeId}")
    public Double findAverageRatingByAdmin(@Parameter(required = true) @PathVariable Long employeeId, Principal principal) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return employeeEvaluationService.findAverageRatingByAdmin(principal.getName(), employeeId, rangeStart, rangeEnd);
    }
}
