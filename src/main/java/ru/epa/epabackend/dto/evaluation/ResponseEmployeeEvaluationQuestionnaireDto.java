package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationShortDto;

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
     * Список оценок руководителя.
     */
    List<ResponseEmployeeEvaluationShortDto> adminEvaluation;

    /**
     * Список оценок коллег.
     */
    List<ResponseEmployeeEvaluationShortDto> usersEvaluation;

    /**
     * Рекомендация руководителя.
     */
    ResponseRecommendationShortDto recommendation;
}
