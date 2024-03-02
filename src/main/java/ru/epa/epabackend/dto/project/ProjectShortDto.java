package ru.epa.epabackend.dto.project;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.epa.epabackend.util.ProjectStatus;

/**
 * Класс ProjectShortDto для передачи короткой информации о проекте
 *
 * @author Константин Осипов
 */
@Data
@AllArgsConstructor
public class ProjectShortDto {
    private Long id;
    private String name;
    private ProjectStatus status;
}
