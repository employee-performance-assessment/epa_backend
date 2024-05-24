package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;

import java.util.List;

/**
 * Класс для получения рейтинга сотрудников по месяцам
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePersonalRatingDto {

    /**
     * Сокращенная информация о сотруднике
     */
    private ResponseEmployeeShortDto employee;

    /**
     * Рейтинг сотрудника по месяцам
     */
    private List<ResponseRatingFullDto> ratingByMonth;
}
