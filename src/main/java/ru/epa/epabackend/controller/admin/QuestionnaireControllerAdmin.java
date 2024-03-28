package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireFullResponseDto;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireRequestDto;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.security.Principal;

/**
 * Класс QuestionnaireControllerAdmin для работы с энпоинтами анкеты, доступ к которым имеет администатор
 */
@Tag(name = "Admin: Анкеты", description = "API администратора для работы с анкетами")
@Validated
@RestController
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RequestMapping("/admin/questionnaire")
public class QuestionnaireControllerAdmin {

    private final QuestionnaireService questionnaireService;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Получение последней заполняемой анкеты администратора
     */
    @Operation(summary = "Получение последней заполняемой анкеты администратора")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto findFullLastByAuthor(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.findLastByAuthorEmail(principal.getName()));
    }

    /**
     * Создание анкеты со статусом CREATED.
     * Создание анкеты возможно если у админа не было ранее анкет или предыдущая имела статус SHARED
     */
    @Operation(summary = "Создание анкеты со статусом CREATED",
            description = "Создание анкеты возможно если у админа не было ранее анкет или предыдущая имела статус SHARED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionnaireFullResponseDto save(@Valid @RequestBody QuestionnaireRequestDto questionnaireRequestDto,
                                             Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.save(questionnaireRequestDto,
                principal.getName()));
    }

    /**
     * Обновление анкеты.
     * Обновление анкеты возможно, если она имеет статус CREATED
     */
    @Operation(summary = "Обновление анкеты", description = "Обновление анкеты возможно, если она имеет статус CREATED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto updateLast(@Valid @RequestBody QuestionnaireRequestDto questionnaireRequestDto,
                                                   Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.updateLast(questionnaireRequestDto,
                principal.getName()));
    }

    /**
     * Первый способ отправки анкет сотрудникам
     * Изменение статуса анкеты с CREATED на SHARED. Статус SHARED имеют анкеты,которые разосланы сотрудникам
     * для проставления оценок. Если у админа нет анкет или статус последней анкеты SHARED, то возвращается ошибка
     */
    @Operation(summary = "Изменение статуса анкеты с CREATED на SHARED",
            description = "Первый способ отправки анкет сотрудникам. Изменение статуса анкеты с CREATED на SHARED. " +
                    "Статус SHARED имеют анкеты,которые разосланы сотрудникам для проставления оценок. " +
                    "Если у админа нет анкет или статус последней анкеты SHARED, то возвращается ошибка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto updateLastQuestionnaireStatusAndDate(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(
                questionnaireService.updateLastQuestionnaireStatusAndDate(QuestionnaireStatus.SHARED, principal.getName()));
    }

    /**
     * Второй способ отправки анкет сотрудникам
     * Дублирование анкеты со статусом SHARED с новой датой создания. Необходимо в случае, когда предыдущая анкета
     * не редактировалась, но имеет также статус SHARED
     * Если у админа последняя анкета имеет статус CREATED, то возвращается ошибка
     */
    @Operation(summary = "Дублирование анкеты со статусом SHARED с новой датой создания",
            description = "Второй способ отправки анкет сотрудникам. Необходимо в случае, когда предыдущая анкета " +
                    "не редактировалась, но имеет также статус SHARED. " +
                    "Если у админа последняя анкета имеет статус CREATED, то возвращается ошибка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionnaireFullResponseDto duplicateLastShared(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.duplicateLastShared(principal.getName()));
    }

    /**
     * Третий способ отправки анкет сотрудникам
     * Создание анкеты с дефолтными критериями и статусом SHARED. Необходимость этого способа в случае отсутствия
     * каких-либо анкет у админа. Если у админа есть анкеты, возвращается ошибка
     */
    @Operation(summary = "Создание анкеты с дефолтными критериями и статусом SHARED",
            description = "Третий способ отправки анкет сотрудникам. Необходимость этого способа в случае отсутствия " +
                    "каких-либо анкет у админа. Если у админа есть анкеты, возвращается ошибка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = QuestionnaireFullResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/default-with-shared")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionnaireFullResponseDto saveDefaultWithSharedStatus(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.saveDefaultWithSharedStatus(principal.getName()));
    }
}