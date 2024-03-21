package ru.epa.epabackend.dto.evaluation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.epa.epabackend.util.ValidationGroups;

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
public class EmployeeEvaluationRequestDto {

    /**
     * Название оценки.
     */
    @NotNull(groups = {ValidationGroups.Create.class})
    @Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long criteriaId;

    /**
     * Количество звезд.
     */
    private Integer score;
}
