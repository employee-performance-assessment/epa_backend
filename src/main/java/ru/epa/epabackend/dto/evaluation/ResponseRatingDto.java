package ru.epa.epabackend.dto.evaluation;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
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
@SqlResultSetMapping(name = "RatingResponseDto",
        classes = @ConstructorResult(targetClass = ResponseRatingDto.class,
                columns = {@ColumnResult(name = "rating"),}))
public class ResponseRatingDto {

    /**
     * Рейтинг сотрудника.
     */
    private Double rating;
}
