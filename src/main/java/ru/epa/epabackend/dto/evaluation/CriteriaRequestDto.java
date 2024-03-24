package ru.epa.epabackend.dto.evaluation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Класс EvaluationRequestDto для передачи тела запроса на сервер для создания критерия оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaRequestDto {

    /**
     * Название критерия оценки.
     */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}