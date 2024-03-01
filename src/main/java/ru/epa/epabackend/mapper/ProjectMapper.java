package ru.epa.epabackend.mapper;

import lombok.experimental.UtilityClass;
import ru.epa.epabackend.dto.project.ProjectOutDtoShort;
import ru.epa.epabackend.model.Project;

/**
 * Класс TaskMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@UtilityClass
public class ProjectMapper {

    public ProjectOutDtoShort projectShortToOutDto(Project project) {
        return ProjectOutDtoShort.builder()
                .name(project.getName())
                .status(project.getStatus())
                .build();
    }
}