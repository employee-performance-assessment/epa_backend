package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.task.TaskCreateFindByIdUpdateResponseDto;
import ru.epa.epabackend.dto.task.TaskCreateUpdateRequestDto;
import ru.epa.epabackend.dto.task.TaskFindAllResponseDto;
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
    Task mapToEntity(TaskCreateUpdateRequestDto taskInDto, Project project, Employee executor);

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    TaskCreateFindByIdUpdateResponseDto mapToFullDto(Task task);

    /**
     * Преобразование из сущности в DTO, краткое.
     */
    TaskFindAllResponseDto mapToShortDto(Task task);
}