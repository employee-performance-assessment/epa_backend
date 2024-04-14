package ru.epa.epabackend.dto.project;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.util.ProjectStatus;

/**
 * Класс ProjectUpdateRequestDto для передачи тела запроса проекта на сервер для обновления
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestProjectUpdateDto {
    @Size(min = 1, max = 255, message = "Допустимая длина названия проекта от 1 до 255 символов")
    private String name;
    @Hidden
    private ProjectStatus status;
}
