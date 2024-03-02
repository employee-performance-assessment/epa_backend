package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.project.ProjectOutDtoShort;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.ProjectStatus;

import java.util.List;

/**
 * Класс ProjectMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов и Константин Осипов
 */

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectOutDtoShort mapToShortDto(Project project);
}
