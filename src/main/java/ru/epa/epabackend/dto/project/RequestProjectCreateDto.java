package ru.epa.epabackend.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_WHITESPACE_AND_DASH;

/**
 * Класс для передачи тела запроса создания проекта на сервер
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestProjectCreateDto {
    @NotBlank(message = "Название не должно быть пустым")
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH,
            message = "В названии проекта разрешены русские, английские символы, пробел и дефис")
    @Size(min = 1, max = 255, message = "Допустимая длина названия проекта от 1 до 255 символов")
    private String name;
}