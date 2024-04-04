package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationShortDto;

import java.util.List;

/**
 * Класс ResponseAdminEvaluationDto для передачи информации о сохраненной оценке поставленной руководителем.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAdminEvaluationDto {

    /**
     * Список оценок руководителя.
     */
    List<ResponseEmployeeEvaluationShortDto> adminEvaluations;

    /**
     * Рекомендация руководителя.
     */
    ResponseRecommendationShortDto recommendation;
}
