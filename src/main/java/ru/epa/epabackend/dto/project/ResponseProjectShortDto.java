package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.util.ProjectStatus;

/**
 * Класс ProjectShortResponseDto для передачи короткой информации о проекте
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
    private ProjectStatus status;
}
