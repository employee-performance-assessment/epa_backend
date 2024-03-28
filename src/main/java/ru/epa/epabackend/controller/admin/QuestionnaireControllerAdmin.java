package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireFullResponseDto;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireRequestDto;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.security.Principal;

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
    @GetMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto findFullLastByAuthor(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.findLastByAuthorEmail(principal.getName()));
    }

    /**
     * Создание анкеты со статусом CREATED.
     * Создание анкеты возможно если у админа не было ранее анкет или предыдущая имела статус SHARED
     */
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
    @PostMapping("/default-with-shared")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionnaireFullResponseDto saveDefaultWithSharedStatus(Principal principal) {
        return questionnaireMapper.mapToFullResponseDto(questionnaireService.saveDefaultWithSharedStatus(principal.getName()));
    }
}
