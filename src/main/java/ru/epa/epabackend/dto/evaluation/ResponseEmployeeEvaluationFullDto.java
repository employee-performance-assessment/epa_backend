package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaShortDto;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;

import java.time.LocalDate;

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
public class ResponseEmployeeEvaluationFullDto {

    /**
     * Идентификатор оценки сотрудника.
     */
    private Long id;

    /**
     * Сотрудник, которого оцениваем.
     */
    private ResponseEmployeeShortDto evaluated;

    /**
     * Сотрудник, который оценивает.
     */
    private ResponseEmployeeShortDto evaluator;

    /**
     * Дата оценки.
     */
    private LocalDate createDay;

    /**
     * Название оценки.
     */
    private ResponseCriteriaShortDto criteria;

    /**
     * Количество звезд.
     */
    private Integer score;
}
