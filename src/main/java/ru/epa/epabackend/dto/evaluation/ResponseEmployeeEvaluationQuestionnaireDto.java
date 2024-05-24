package ru.epa.epabackend.dto.evaluation;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;

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
public class ResponseEmployeeEvaluationQuestionnaireDto {

    /**
     * Дата создания/рассылки анкеты
     */
    private LocalDate createQuestionnaire;

    /**
     * Средний бал за анкету
     */
    private Double middleScore;

    /**
     * Список оценок
     */
    private HashMap<String, ResponseEvaluationsAdminUserDto> evaluations;

    /**
     * Рекомендация руководителя
     */
    private String recommendation;
}
