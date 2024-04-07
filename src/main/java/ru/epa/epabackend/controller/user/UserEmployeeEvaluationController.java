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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;
import static ru.epa.epabackend.util.DateConstant.DATE_TIME_PATTERN;

/**
 * Класс EmployeeEvaluationControllerUser содержит эндпойнты для авторизованного пользователя, относящиеся к
 * оценке сотрудником коллег и получению своих оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Private: Оценки Сотрудников", description = "Закрытый API для работы с оценками сотрудников")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/evaluations")
public class UserEmployeeEvaluationController {

    private final EmployeeEvaluationService employeeEvaluationService;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт добавления оценок сотрудника
     */
    @Operation(summary = "Сохранения оценок сотрудника своего коллеги",
            description = "При успешном добавлении возвращается код 201 Created.")
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
     * Эндпойнт получения сотрудником всех оценок и рекомендации по ID анкеты.
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
    public List<ResponseEmployeeShortDto> findAllRatedByMe(Principal principal) {
        List<Employee> employees = employeeEvaluationService.findAllRatedByMe(principal.getName());
        return employeeMapper.mapList(employees);
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
        return employeeEvaluationService.findCommandRating(principal.getName());
    }

    /**
     * Эндпойнт получения личного рейтинга.
     */
    @Operation(summary = "Получение личного рейтинга",
            description = "Возвращает личный рейтинг за каждый оцененный месяц" +
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
    public List<ResponseRatingFullDto> findPersonalRating(Principal principal) {
        return employeeEvaluationService.findPersonalRating(principal.getName());
    }

    /**
     * Эндпойнт получения списка поставленных оценок по ID оцененного сотрудника.
     */
    @Operation(
            summary = "Получение сотрудником списка поставленных оценок по ID оцененного сотрудника",
            description = "Возвращает список поставленных оценок" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseMyEvaluationsDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/my")
    public List<ResponseMyEvaluationsDto> findAllMyEvaluationsEvaluatedId(Principal principal, @RequestParam Long evaluatedId) {
        return employeeEvaluationService.findAllMyEvaluationsByEvaluatedId(principal.getName(), evaluatedId);
    }

    /**
     * Эндпоинт получения списка сотрудиков и анкет, по которым их необходимо оценить
     */
    @Operation(summary = "Эндпоинт получения списка сотрудиков и анкет, по которым их необходимо оценить")
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
     * Эндпоинт получения списка сотрудиков и анкет, по которым ставились оценки ранее
     */
    @Operation(summary = "Эндпоинт получения списка сотрудиков и анкет, по которым ставились оценки ранее")
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
     * Эндпойнт получения сотрудником оценок по ID анкеты и ID сотрудника в разделе Оцени сотрудника.
     */
    @Operation(
            summary = "Эндпойнт получения сотрудником оценок по ID анкеты и ID сотрудника в разделе Оцени сотрудника ",
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
     * Эндпойнт получения сотрудником своего среднего рейтинга за текущий месяц.
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
    @GetMapping("/rating/{employeeId}")
    public Double findAverageRatingByUser(Principal principal) {
        LocalDate rangeStart = YearMonth.now().atDay(1);
        LocalDate rangeEnd = YearMonth.now().atEndOfMonth();
        return employeeEvaluationService.findAverageRatingByUser(principal, rangeStart, rangeEnd);
    }
}
