package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Класс содержит эндпойнты для авторизованного пользователя, относящиеся к оценке сотрудником коллег и получению
 * своих оценок
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Private: Оценки Сотрудников", description = "API пользователя для работы с оценками сотрудников")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/evaluations")
public class UserEmployeeEvaluationController {

    private final EmployeeEvaluationService employeeEvaluationService;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;

    /**
     * Добавление оценок сотрудника
     */
    @Operation(summary = "Добавление оценок сотрудника по анкете")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeEvaluationFullDto.class)))),
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
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResponseEmployeeEvaluationFullDto> save(Principal principal,
            @RequestParam Long questionnaireId, @RequestParam Long evaluatedId,
            @Valid @RequestBody List<RequestEmployeeEvaluationDto> evaluationListRequestDto) {
        List<EmployeeEvaluation> employeeEvaluations = employeeEvaluationService.create(principal.getName(),
                        evaluatedId, questionnaireId, evaluationListRequestDto);
        return employeeEvaluationMapper.mapList(employeeEvaluations);
    }

    /**
     * Получение сотрудником всех оценок и рекомендации по ID анкеты
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
    @GetMapping()
    public ResponseEmployeeEvaluationQuestionnaireDto findEvaluationsAndRecommendation(
            Principal principal, @RequestParam Long questionnaireId) {
        return employeeEvaluationService.findAllEvaluationsByQuestionnaireId(principal.getName(),
                questionnaireId);
    }

    /**
     * Получение командного рейтинга по месяца указанного года
     */
    @Operation(summary = "Получение командного рейтинга по месяцам указанного года. ",
            description = "Возвращает список рейтинга команды за каждый оцененный месяц" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает пустой список.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseRatingFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating/command")
    public List<ResponseRatingFullDto> findCommandRating(Principal principal, @RequestParam Integer year) {
        return employeeEvaluationService.findCommandRating(principal.getName(), year);
    }

    /**
     * Получения личного рейтинга по месяцам года
     */
    @Operation(summary = "Получения личного рейтинга по месяцам года",
            description = "В случае, если не найдено ни одной оценки, возвращает пустой список.")
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
    public List<ResponseRatingFullDto> findPersonalRating(Principal principal, @RequestParam Integer year) {
        return employeeEvaluationService.findPersonalRating(principal.getName(), year);
    }

    /**
     * Получение списка сотрудиков и анкет, по которым их необходимо оценить
     */
    @Operation(summary = "Получение списка сотрудиков и анкет, по которым их необходимо оценить")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeAssessDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/assess-list")
    public List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessment(
            Principal principal,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        return employeeEvaluationService.findEmployeesQuestionnairesForAssessment(principal.getName(), text, from, to);
    }

    /**
     * Получение списка сотрудиков и анкет, по которым ставились оценки ранее
     */
    @Operation(summary = "Получение списка сотрудиков и анкет, по которым ставились оценки ранее")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseEmployeeAssessDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/assessed-list")
    public List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesAssessed(
            Principal principal,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        return employeeEvaluationService.findEmployeesQuestionnairesAssessed(principal.getName(), text, from, to);
    }

    /**
     * Получение сотрудником оценок по id анкеты и сотрудника в разделе Оцени сотрудника
     */
    @Operation(
            summary = "Получение сотрудником оценок по id анкеты и сотрудника в разделе Оцени сотрудника ",
            description = "Возвращает список оценок по заполненной анкете для указанного сотрудника."
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
    public List<ResponseEmployeeEvaluationShortDto> findAssessedQuestionnaire(
            Principal principal, @RequestParam Long questionnaireId, @RequestParam Long evaluatedId) {
        return employeeEvaluationService.findQuestionnaireScores(principal.getName(), questionnaireId, evaluatedId);
    }

    /**
     * Получение сотрудником своего среднего рейтинга за текущий месяц
     */
    @Operation(summary = "Получение сотрудником своего среднего рейтинга за текущий месяц")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/rating")
    public Double findAverageRatingByUser(Principal principal) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return employeeEvaluationService.findAverageRatingByUser(principal, rangeStart, rangeEnd);
    }

    /**
     * Получение сотрудником списка анкет, в которых он оценен
     */
    @Operation(
            summary = "Получение сотрудником списка анкет, в которых он оценен",
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
    public List<ResponseEvaluatedQuestionnaireDto> findListQuestionnaireByEvaluatedEmail(
            Principal principal,
            @RequestParam(required = false) @Min(0) @Max(5) Integer stars,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        List<ResponseEvaluatedQuestionnaireDto> listQuestionnaire = employeeEvaluationService
                .findAllQuestionnaireByEvaluatedEmail(principal.getName(), stars, from, to);
        return listQuestionnaire;
    }
}
