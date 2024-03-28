package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireFullResponseDto;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireShortResponseDto;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.security.Principal;

@Tag(name = "Private: Анкеты", description = "API пользователя для работы с анкетами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/questionnaire")
public class QuestionnaireControllerUser {
    private final QuestionnaireService questionnaireService;
    private final QuestionnaireMapper questionnaireMapper;
    private final EmployeeService employeeService;

    /**
     * Получение короткой информации о последней заполненной администратором анкете со статусом SHARED
     * В случае отсутствия у администратора анкеты со статусом SHARED возвращается ошибка
     */
    @GetMapping("/last-shared")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireShortResponseDto findShortLastByAuthorAndStatus(Principal principal) {
        return questionnaireMapper.mapToShortResponseDto(questionnaireService.findLastByAuthorAndStatus(principal.getName(),
                QuestionnaireStatus.SHARED));
    }

    /**
     * Получение полной информации об анкете по id
     * Если анкету получает кто-то, чей админ не является автором анкеты, то возвращается ошибка
     */
    @GetMapping("/{questionnaireId}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionnaireFullResponseDto findFullByEmailAndId(Principal principal, @PathVariable long questionnaireId) {
        return questionnaireMapper.mapToFullResponseDto(
                questionnaireService.findByEmailAndId(principal.getName(), questionnaireId));
    }
}