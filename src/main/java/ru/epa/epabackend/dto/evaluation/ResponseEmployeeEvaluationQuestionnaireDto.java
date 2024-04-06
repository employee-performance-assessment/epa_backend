package ru.epa.epabackend.dto.evaluation;

import lombok.*;

import java.util.HashMap;

/**
 * Класс ResponseEmployeeEvaluationQuestionnaireDto для передачи полной информации об оценке сотрудника.
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
     * Список оценок.
     */
    private HashMap<String, ResponseEvaluationsAdminUserDto> evaluations;

    /**
     * Рекомендация руководителя.
     */
    private String recommendation;
}
