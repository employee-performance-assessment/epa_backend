package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.task.*;
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
    @Mapping(target = "status", source = "taskRequestDto.status")
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "finishDate", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "name", source = "taskRequestDto.name")
    Task mapToEntity(TaskRequestDto taskRequestDto, Project project, Employee executor, Employee owner);

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    TaskFullResponseDto mapToFullDto(Task task);

    TaskAnalyticsFullResponseDto mapToAnalyticsDto(Double teamCompletedOnTimePercent,
                                                   Double teamNotCompletedOnTimePercent,
                                                   List<EmployeeShortResponseDto> leaders,
                                                   List<EmployeeShortResponseDto> deadlineViolators
                                          );

    TaskAnalyticsShortResponseDto mapToAnalyticsDto(Double teamCompletedOnTimePercent,
                                                    Double teamNotCompletedOnTimePercent);

    /**
     * Преобразование из списка задач в список с краткой информацией о задаче.
     */
    List<TaskShortResponseDto> mapList(List<Task> tasks);

    /**
     * Обновление полей при обновлении задачи.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "taskCreateUpdateRequestDto.status")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "executor", source = "executor")
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "finishDate", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "name", source = "taskCreateUpdateRequestDto.name")
    Task updateFields(TaskRequestDto taskCreateUpdateRequestDto, Project project, Employee executor,
                      @MappingTarget Task oldTask);
}