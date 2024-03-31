package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.RequestQuestionnaireDto;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireFullDto;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.ValidationGroups.Update;

import java.security.Principal;

/**
 * Класс QuestionnaireControllerAdmin для работы с энпоинтами анкеты, доступ к которым имеет администатор.
 * Администратор может создавать анкеты, тогда статус анкет будет CREATED, изменять CREATED анкеты без изменения их id.
 * Сотрудники не видят анкеты администратора со статусом CREATED.
 * Администратор может изменять статус анкеты на SHARED,
 * что равносильно отправке анкеты сотрудникам для проставления оценок.
 * Отправлять повторно SHARED анкеты, тогда создаётся анкета с новым id, датой и статусом SHARED.
 * Админ и без анкет может создать SHARED анкету с дефолтным списком критериев,
 * что равносильно отправке такой анкеты сотрудникам для проставления оценок
 * Изменение анкеты со статусом SHARED невозможно, что гарантирует отсутствие последующих изменений
 * в анкете после отправки её сотрудникам.
 * Если последняя анкета имела статус SHARED, то при запросе последней анкеты возвращается новая анкета со статусом CREATED
 */
@Tag(name = "Admin: Анкеты", description = "API администратора для работы с анкетами")
@Validated
@RestController
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RequestMapping("/admin/questionnaire")
public class AdminQuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Получение последней заполняемой анкеты администратора
     * Возвращается последняя анкета со статусом CREATED
     * Если анкеты не существует, то создаётся и возвращается анкета с дефолтными критериями
     * Если анкета существует, но со статусом SHARED, то создаётся новая анкета
     */
    @Operation(summary = "Получение последней заполняемой анкеты администратора",
            description = "Возвращается последняя анкета со статусом CREATED. " +
                    "Если анкеты не существует, то создаётся и возвращается анкета с дефолтными критериями. " +
                    "Если анкета существует, но со статусом SHARED, то создаётся новая анкета.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseQuestionnaireFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/last")
    public ResponseQuestionnaireFullDto findFullLastByAuthor(Principal principal) {
        Questionnaire questionnaire = questionnaireService.findLastByAuthorEmail(principal.getName());
        return questionnaireMapper.mapToFullResponseDto(questionnaire);
    }

    /**
     * Обновление последней анкеты
     */
    @Operation(summary = "Обновление анкеты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseQuestionnaireFullDto.class))),
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
    @PatchMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public ResponseQuestionnaireFullDto updateLast(@Validated(Update.class) @RequestBody RequestQuestionnaireDto requestQuestionnaireDto,
                                                   Principal principal) {
        Questionnaire questionnaire = questionnaireService.updateLast(requestQuestionnaireDto, principal.getName());
        return questionnaireMapper.mapToFullResponseDto(questionnaire);
    }

    /**
     * Отправка последней анкеты сотрудникам
     * Изменение статуса анкеты с CREATED на SHARED. Статус SHARED имеют анкеты,которые разосланы сотрудникам
     * для проставления оценок.
     * Если у админа нет анкет, то создаётся анкета с дефолтными критериями и статусом SHARED.
     * Если у админа уже есть анкета со статусом SHARED, то создаётся её дубликат с новым id и новой датой
     */
    @Operation(summary = "Отправка анкеты сотрудникам",
            description = "Изменение статуса анкеты с CREATED на SHARED. Статус SHARED имеют анкеты,которые " +
                    "разосланы сотрудникам для проставления оценок." +
                    "Если у админа нет анкет, то создаётся анкета с дефолтными критериями и статусом SHARED. " +
                    "Если у админа уже есть анкета со статусом SHARED, то создаётся её дубликат с новым id и новой " +
                    "датой.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseQuestionnaireFullDto.class))),
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
    @PutMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public ResponseQuestionnaireFullDto sendQuestionnaireToEmployees(Principal principal) {
        Questionnaire questionnaire = questionnaireService
                .sendQuestionnaireToEmployees(principal.getName());
        return questionnaireMapper.mapToFullResponseDto(questionnaire);
    }
}