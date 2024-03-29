package ru.epa.epabackend.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс ProjectCreateRequestDto для передачи тела запроса создания проекта на сервер
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreateRequestDto {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}