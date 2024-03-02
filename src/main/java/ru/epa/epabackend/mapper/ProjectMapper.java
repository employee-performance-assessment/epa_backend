package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.project.ProjectOutDtoShort;
import ru.epa.epabackend.model.Project;

/**
 * Интерфейс TaskMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectOutDtoShort mapToShortDto(Project project);
}