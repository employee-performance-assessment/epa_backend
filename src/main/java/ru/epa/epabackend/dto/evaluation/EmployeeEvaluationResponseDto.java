package ru.epa.epabackend.dto.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.ValidationGroups;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Класс EvaluationCreateResponseDto для передачи для передачи информации об оценках сотрудника
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
    //@NotNull(groups = {ValidationGroups.Create.class})
    //@Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private HashMap<EvaluationResponseDto, Double> evaluationId;
}
