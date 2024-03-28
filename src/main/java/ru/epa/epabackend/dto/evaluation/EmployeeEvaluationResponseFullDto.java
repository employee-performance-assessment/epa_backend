package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.criteria.CriteriaShortResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;

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
public class EmployeeEvaluationResponseFullDto {

    /**
     * Идентификатор оценки сотрудника.
     */
    private Long id;

    /**
     * Сотрудник, которого оцениваем.
     */
    private EmployeeShortResponseDto evaluated;

    /**
     * Сотрудник, который оценивает.
     */
    private EmployeeShortResponseDto evaluator;

    /**
     * Дата оценки.
     */
    private LocalDate createDay;

    /**
     * Название оценки.
     */
    private CriteriaShortResponseDto criteria;

    /**
     * Количество звезд.
     */
    private Integer score;
}
