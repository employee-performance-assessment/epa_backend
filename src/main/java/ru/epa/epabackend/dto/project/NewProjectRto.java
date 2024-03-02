package ru.epa.epabackend.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Класс NewProjectRto для передачи тела запроса создания проекта на сервер
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewProjectRto {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;
}
