package ru.epa.epabackend.dto.evaluation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS;

/**
 * Класс для передачи тела запроса на сервер для создания оценки
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAdminEvaluationDto {

    /**
     * Список оценок руководителя
     */
    private List<@Valid RequestEmployeeEvaluationDto> evaluationDtoList;

    /**
     * Описание рекомендации
     */
    @NotBlank(message = "Поле рекомендация не должно быть пустым")
    @Size(min = 1, max = 255, message = "Допустимая длина поля рекомендации от 1 до 255 символов")
    @Pattern(regexp = CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS,
            message = "В поле рекомендация разрешены русские, английские символы, цифры, пробел и спецсимволы " +
                    ",:;?!*+%-<>@[]/_{}$#")
    private String recommendation;
}