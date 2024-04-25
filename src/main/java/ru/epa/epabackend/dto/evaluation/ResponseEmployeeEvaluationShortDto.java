package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс для получения оценки
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEmployeeEvaluationShortDto {

    /**
     * Название критерия
     */
    private String name;

    /**
     * Количество звезд с округлением до одного знака после запятой
     */
    private Double score;
}
