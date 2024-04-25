package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaShortDto;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireShortDto;

import java.time.LocalDate;

/**
 * Класс для передачи полной информации об оценке сотрудника
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEmployeeEvaluationFullDto {

    /**
     * Идентификатор оценки сотрудника
     */
    private Long id;

    /**
     * Сотрудник, которого оцениваем
     */
    private ResponseEmployeeShortDto evaluated;

    /**
     * Сотрудник, который оценивает
     */
    private ResponseEmployeeShortDto evaluator;

    /**
     * Дата оценки
     */
    private LocalDate createDay;

    /**
     * Анкета по которой оцениваем
     */
    private ResponseQuestionnaireShortDto questionnaire;

    /**
     * Название оценки
     */
    private ResponseCriteriaShortDto criteria;

    /**
     * Количество звезд
     */
    private Integer score;
}
