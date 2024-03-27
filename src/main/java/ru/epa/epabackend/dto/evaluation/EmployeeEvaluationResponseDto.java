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
@SqlResultSetMapping(name = "EmployeeEvaluationResponseDto",
        classes = @ConstructorResult(targetClass = EmployeeEvaluationResponseDto.class,
                columns = {@ColumnResult(name = "name"),
                        @ColumnResult(name = "score"),}))
public class EmployeeEvaluationResponseDto {

    /**
     * Название критерия.
     */
    private String name;

    /**
     * Количество звезд с округлением до одного знака после запятой.
     */
    private Double score;

    public EmployeeEvaluationResponseDto(Object name, Double score) {
        this.name = name.toString();
        this.score = score;
    }
}
