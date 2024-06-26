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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireFullDto;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireShortDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.security.Principal;
import java.util.List;

/**
 * Класс для работы с энпоинтами анкеты, доступ к которым имеют зарегистрированные
 * пользователи.
 * Пользователи могут запрашивать анкеты:
 * -последнюю опубликованную админом (со статусом SHARED),
 * -по id анкеты. Анкета должна относится к админу сотрудника
 * -все опубликованные (SHARED) анкеты админа
 */
@Tag(name = "Private: Анкеты", description = "API пользователя для работы с анкетами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/questionnaire")
public class UserQuestionnaireController {
    private final QuestionnaireService questionnaireService;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Получение полной информации об отправленной (SHARED) анкете по id
     * Если анкету получает кто-то, чей админ не является автором анкеты, то возвращается ошибка
     * Если анкета имеет статус CREATED, то возвращается ошибка
     */
    @Operation(summary = "Получение полной информации об отправленной (SHARED) анкете по id",
            description = "Если анкету получает кто-то, чей админ не является автором анкеты, то возвращается ошибка. " +
                    "Если анкета имеет статус CREATED, то возвращается ошибка")
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
    @GetMapping("/{questionnaireId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseQuestionnaireFullDto findFullByEmailAndId(Principal principal, @PathVariable long questionnaireId) {
        Questionnaire questionnaire = questionnaireService.findByEmailAndId(principal.getName(), questionnaireId);
        return questionnaireMapper.mapToFullResponseDto(questionnaire);
    }

    /**
     * Получение короткой информации об опубликованных (SHARED) анкетах админа
     * В случае отсутствия опубликованных анкет возвращается пустой список
     */
    @Operation(summary = "Получение короткой информации об опубликованных (SHARED) анкетах админа",
            description = "В случае отсутствия опубликованных анкет возвращается пустой список")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = ResponseQuestionnaireShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/all-shared")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseQuestionnaireShortDto> findAllShared(Principal principal) {
        List<Questionnaire> questionnairies = questionnaireService.findAllByAuthorIdAndStatus(principal.getName(),
                QuestionnaireStatus.SHARED);
        return questionnaireMapper.mapToList(questionnairies);
    }
}