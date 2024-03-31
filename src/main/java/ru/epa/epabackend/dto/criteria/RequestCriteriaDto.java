package ru.epa.epabackend.dto.criteria;

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
public class RequestCriteriaDto {

    /**
     * Название критерия оценки.
     */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
