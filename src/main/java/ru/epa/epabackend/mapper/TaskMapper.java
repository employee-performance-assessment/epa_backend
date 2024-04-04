package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.task.ResponseTaskFullDto;
import ru.epa.epabackend.dto.task.RequestTaskDto;
import ru.epa.epabackend.dto.task.ResponseTaskShortDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;

import java.util.List;

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
    @Mapping(target = "status", source = "requestTaskDto.status")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "finishDate", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "name", source = "requestTaskDto.name")
    Task mapToEntity(RequestTaskDto requestTaskDto, Project project, Employee executor, Employee owner);

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    ResponseTaskFullDto mapToFullDto(Task task);

    /**
     * Преобразование из списка задач в список с краткой информацией о задаче.
     */
    List<ResponseTaskShortDto> mapList(List<Task> tasks);

    /**
     * Обновление полей при обновлении задачи.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "requestTaskDto.status")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "executor", source = "executor")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "finishDate", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "name", source = "requestTaskDto.name")
    Task updateFields(RequestTaskDto requestTaskDto, Project project, Employee executor,
                      @MappingTarget Task oldTask);
}