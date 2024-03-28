package ru.epa.epabackend.controller.user;

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
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireFullResponseDto;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireShortResponseDto;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс QuestionnaireControllerUser для работы с энпоинтами анкеты, доступ к которым имеют зарегистрированные
 * пользователи
 */
@Tag(name = "Private: Анкеты", description = "API пользователя для работы с анкетами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/questionnaire")
public class QuestionnaireControllerUser {
    private final QuestionnaireService questionnaireService;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Получение короткой информации о последней заполненной администратором анкете со статусом SHARED
     * В случае отсутствия у администратора анкеты со статусом SHARED возвращается ошибка
     */
    @Operation(summary = "Получение короткой информации о последней заполненной администратором анкете со статусом SHARED",
            description = "В случае отсутствия у администратора анкеты со статусом SHARED возвращается ошибка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireShortResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/last-shared")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireShortResponseDto findShortLastByAuthorAndStatus(Principal principal) {
        return questionnaireMapper.mapToShortResponseDto(questionnaireService.findLastByAuthorAndStatus(principal.getName(),
                QuestionnaireStatus.SHARED));
    }

    /**
     * Получение полной информации об отправленной (SHARED) анкете по id
     * Если анкету получает кто-то, чей админ не является автором анкеты, то возвращается ошибка
     * Если анкета имеет статус CREATED, то возвращается ошибка
     */
    @Operation(summary = "олучение полной информации об отправленной (SHARED) анкете по id",
            description = "Если анкету получает кто-то, чей админ не является автором анкеты, то возвращается ошибка. " +
                    "Если анкета имеет статус CREATED, то возвращается ошибка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{questionnaireId}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto findFullByEmailAndId(Principal principal, @PathVariable long questionnaireId) {
        return questionnaireMapper.mapToFullResponseDto(
                questionnaireService.findByEmailAndId(principal.getName(), questionnaireId));
    }

    /**
     * Получение короткой информации об опубликованных (SHARED) анкетах администратора
     * В случае отсутствия опубликованных анкет возвращается пустой список
     */
    @Operation(summary = "Получение короткой информации об опубликованных (SHARED) анкетах администратора",
            description = "В случае отсутствия опубликованных анкет возвращается пустой список")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireShortResponseDto.class)))})
    @GetMapping("/all-shared")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionnaireShortResponseDto> findAllShared(Principal principal) {
        return questionnaireService.findAllByAuthorIdAndStatus(principal.getName(),
                        QuestionnaireStatus.SHARED).stream().map(questionnaireMapper::mapToShortResponseDto)
                .collect(Collectors.toList());
    }
}