package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс EvaluationCreateResponseDto для передачи информации об оценках сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEmployeeEvaluationDto {

    /**
     * Название критерия.
     */
    private String name;

    /**
     * Количество звезд с округлением до одного знака после запятой.
     */
    private Double score;
}
