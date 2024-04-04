package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.recommendation.RequestRecommendationDto;

import java.util.List;

/**
 * Класс EvaluationCreateRequestDto для передачи тела запроса на сервер для создания оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAdminEvaluationDto {

    /**
     * Список оценок руководителя.
     */
    List<RequestEmployeeEvaluationDto> evaluationDtoList;

    /**
     * Рекомендация руководителя.
     */
    RequestRecommendationDto recommendation;
}