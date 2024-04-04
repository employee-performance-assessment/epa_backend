package ru.epa.epabackend.dto.criteria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS;

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
    @NotBlank()
    @Size(min = 1, max = 100)
    @Pattern(regexp = CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS)
    private String name;
}
