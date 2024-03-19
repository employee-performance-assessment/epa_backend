package ru.epa.epabackend.dto.evaluation;

import lombok.*;

import java.util.HashMap;

/**
 * Класс EvaluationCreateResponseDto для передачи для передачи информации об оценках сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEvaluationResponseDto {

    /**
     * Сотрудник, которого оценили.
     */
    private Long appraiserId;

    /**
     * Список оценок.
     */
    private HashMap<EvaluationResponseDto, Double> evaluations;
}
