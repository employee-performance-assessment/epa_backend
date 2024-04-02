package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс RatingResponseDto для получения рейтинга сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRatingDto {

    /**
     * Рейтинг сотрудника.
     */
    private Double rating;
}
