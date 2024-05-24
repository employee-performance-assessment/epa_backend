package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс для получения рейтинга сотрудника или команды по месяцам
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRatingFullDto {

    /**
     * Номер месяца в котором произведен расчет рейтинга
     */
    private Integer monthNumber;

    /**
     * Рейтинг сотрудника
     */
    private Double rating;
}
