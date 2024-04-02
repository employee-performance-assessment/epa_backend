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
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.security.Principal;
import java.util.List;

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
     * Эндпойнт получения сотрудником всех оценок коллег о себе.
     */
    @Operation(
            summary = "Получение сотрудником всех своих оценок от коллег",
            description = "Возвращает список всех оценок коллег (усредненный по оценкам)" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
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
    @GetMapping()
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsUsers(Principal principal) {
        return employeeEvaluationService.findAllEvaluationsUsers(principal.getName());
    }

    /**
     * Эндпойнт получения сотрудником всех оценок руководителя о себе.
     */
    @Operation(
            summary = "Получение сотрудником своих оценок от руководителя",
            description = "Возвращает список оценок руководителя о себе" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
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
    @GetMapping("/admin")
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdmin(Principal principal) {
        return employeeEvaluationService.findAllEvaluationsAdmin(principal.getName());
    }

    /**
     * Эндпойнт получения списка поставленных оценок сотрудника по ID анкеты.
     */
    @Operation(
            summary = "Получение сотрудником списка оцененных коллег и поставленных оценок по ID анкеты",
            description = "Возвращает список поставленных оценок сотрудника" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
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
    @GetMapping("/my")
    public List<ResponseMyEvaluationsDto> findAllMyEvaluationsById(Principal principal,
                                                                   @RequestParam Long questionnaireId) {
        return employeeEvaluationService.findAllMyEvaluationsById(principal.getName(), questionnaireId);
    }

    /**
     * Эндпойнт получения списка поставленных оценок сотрудника по всем анкетам.
     */
    @Operation(
            summary = "Получение сотрудником списка оцененных коллег и поставленных оценок по всем анкетам",
            description = "Возвращает список поставленных оценок сотрудника" +
                    "\n\nВ случае, если не найдено ни одной оценки, возвращает пустой список."
    )
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
    @GetMapping("/my/all")
    public List<ResponseMyEvaluationsDto> findAllMyEvaluations(Principal principal) {
        return employeeEvaluationService.findAllMyEvaluations(principal.getName());
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
}
