package ru.epa.epabackend.dto.evaluation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Range;
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
public class RequestEmployeeEvaluationDto {

    /**
     * Название оценки.
     */
    @NotNull(groups = {ValidationGroups.Create.class}, message = "Необходимо указать id критерия")
    @Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "id критерия должен " +
            "быть положительным числом")
    private Long criteriaId;

    /**
     * Количество звезд.
     */
    @Range(min = 1, max = 5, message = "Допустимый диапазон оценки от 1 до 5")
    private Integer score;
}
