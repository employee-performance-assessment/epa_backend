package ru.epa.epabackend.dto.project;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.util.ProjectStatus;

/**
 * Класс для передачи короткой информации о проекте
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProjectShortDto {
    private Long id;
    private String name;
    @Hidden
    private ProjectStatus status;
}
