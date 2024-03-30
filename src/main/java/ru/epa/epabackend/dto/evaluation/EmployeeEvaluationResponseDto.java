package ru.epa.epabackend.dto.evaluation;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
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
public class EmployeeEvaluationResponseDto {

    /**
     * Название критерия.
     */
    private String name;

    /**
     * Количество звезд с округлением до одного знака после запятой.
     */
    private Double score;
}
