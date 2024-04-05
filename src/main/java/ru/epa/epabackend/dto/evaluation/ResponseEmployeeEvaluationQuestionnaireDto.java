package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationShortDto;

import java.util.HashMap;
import java.util.List;

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
    private ResponseRecommendationShortDto recommendation;
}
