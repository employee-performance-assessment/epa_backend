package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
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
    Task mapToEntity(TaskInDto taskInDto);

    /**
     *
     * Преобразование из сущности в DTO, при создании.
     */
    TaskFullDto mapToFullDto(Task task);

    /**
     * Преобразование из сущности в DTO, краткое.
     */
    TaskShortDto mapToShortDto(Task task);
}
