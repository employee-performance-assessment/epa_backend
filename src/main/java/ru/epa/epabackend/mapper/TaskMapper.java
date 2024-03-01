package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.model.Task;

/**
 * Интерфейс TaskMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */

@Mapper(componentModel = "spring")
public interface TaskMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    ProjectMapper INSTANCE_ = Mappers.getMapper(ProjectMapper.class);

    /**
     * Преобразование из DTO в сущность.
     */
    Task taskInDtoToTask(TaskInDto taskInDto);
    /**
     * Преобразование из сущности в DTO, при создании.
     */
    TaskFullDto taskToTaskFullDto(Task task);

    /**
     * Преобразование из сущности в DTO, краткое.
     */
    TaskShortDto taskToTaskShortDto(Task task);

    /**
     * Преобразование из сущности в DTO, при обновлении.
     */
    TaskFullDto taskUpdateToOutDto(Task task);
}