package ru.epa.epabackend.mapper;

import org.springframework.stereotype.Component;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.model.Task;

import java.util.List;

/**
 * Класс TaskMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Component
public class TaskMapper {

    /**
     * Преобразование из DTO в сущность.
     */
    public Task dtoInToTask(TaskInDto taskInDto) {
        return Task.builder()
                .name(taskInDto.getName())
                .description(taskInDto.getDescription())
                .basicPoints(taskInDto.getBasicPoints())
                .deadLine(taskInDto.getDeadLine())
                .penaltyPoints(taskInDto.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    public TaskFullDto taskCreateToOutDto(Task task) {
        return TaskFullDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .executor(EmployeeMapper.toEmployeeDtoShort(task.getExecutor()))
                .deadLine(task.getDeadLine())
                .status(task.getStatus())
                .project(ProjectMapper.projectShortToOutDto(task.getProject()))
                .basicPoints(task.getBasicPoints())
                .penaltyPoints(task.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из сущности в DTO, краткое.
     */
    public TaskShortDto taskShortToOutDto(Task task) {
        return TaskShortDto.builder()
                .id(task.getId())
                .name(task.getName())
                .deadLine(task.getDeadLine())
                .status(task.getStatus())
                .basicPoints(task.getBasicPoints())
                .build();
    }

    /**
     * Преобразование из сущности в DTO, при обновлении.
     */
    public TaskFullDto taskUpdateToOutDto(Task task) {
        return TaskFullDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .executor(EmployeeMapper.toEmployeeDtoShort(task.getExecutor()))
                .startDate(task.getStartDate())
                .finishDate(task.getFinishDate())
                .project(ProjectMapper.projectShortToOutDto(task.getProject()))
                .status(task.getStatus())
                .basicPoints(task.getBasicPoints())
                .penaltyPoints(task.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из списка сущностей в список DTO.
     */
    public List<TaskShortDto> tasksToListOutDto(List<Task> listTasks) {
        return listTasks.stream().map(this::taskShortToOutDto).toList();
    }
}