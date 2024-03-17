package ru.epa.epabackend.dto.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.ValidationGroups;

import java.time.LocalDate;

/**
 * Класс EvaluationCreateRequestDto для передачи тела запроса на сервер для создания оценки
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEvaluationRequestDto {

    /**
     * Сотрудник, которого оцениваем.
     */
    private Long appraiserId;

    /**
     * Сотрудник, который оценивает.
     */
    private Long employeeId;

    /**
     * Дата оценки.
     */
    @NotNull(groups = {ValidationGroups.Create.class})
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate dayCreate;

    /**
     * Название оценки.
     */
    @NotNull(groups = {ValidationGroups.Create.class})
    @Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long evaluationId;
}
