package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.util.ProjectStatus;

/**
 * Класс ProjectShortDto для передачи короткой информации о проекте
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectShortDto {
    private Long id;
    private String name;
    private ProjectStatus status;
}
