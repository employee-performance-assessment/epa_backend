package ru.epa.epabackend.dto.evaluation;

import lombok.*;
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
public class EmployeeEvaluationDto {

    /**
     * Идентификатор оценки сотрудника.
     */
    private Long id;

    /**
     * Сотрудник, которого оцениваем.
     */
    private EmployeeShortResponseDto appraiser;

    /**
     * Сотрудник, который оценивает.
     */
    private EmployeeShortResponseDto employee;

    /**
     * Дата оценки.
     */
    private LocalDate createDay;

    /**
     * Название оценки.
     */
    private EvaluationResponseDto evaluation;

    /**
     * Количество звезд.
     */
    private Integer countStar;
}
