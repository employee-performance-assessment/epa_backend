package ru.epa.epabackend.dto.recommendation;

import lombok.*;

/**
 * Класс RecommendationResponseDto для передачи информации о рекомендации сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRecommendationShortDto {

    /**
     * Описание рекомендации.
     */
    private String recommendation;
}
