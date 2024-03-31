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
 * Класс ProjectCreateRequestDto для передачи тела запроса создания проекта на сервер
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestProjectCreateDto {
    @NotBlank
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH)
    @Size(min = 1, max = 255)
    private String name;
}