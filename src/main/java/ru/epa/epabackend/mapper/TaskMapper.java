package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;

/**
 * Интерфейс TaskMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, EmployeeMapper.class})
public interface TaskMapper {

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "taskInDto.status")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "executor", source = "executor")
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "finishDate", ignore = true)
    @Mapping(target = "name", source = "taskInDto.name")
    Task mapToEntity(TaskInDto taskInDto, Project project, Employee executor);

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    TaskFullDto mapToFullDto(Task task);

    /**
     * Преобразование из сущности в DTO, краткое.
     */
    TaskShortDto mapToShortDto(Task task);

}