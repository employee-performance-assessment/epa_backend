package ru.epa.epabackend.mapper;

import org.springframework.stereotype.Component;
import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.dto.TaskOutDto;
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
                .finishDate(taskInDto.getFinishDate())
                .basicPoints(taskInDto.getBasicPoints())
                .penaltyPoints(taskInDto.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из сущности в DTO, при создании.
     */
    public TaskOutDto taskCreateToOutDto(Task task) {
        return TaskOutDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .creator(task.getCreator())
                .executor(task.getExecutor())
                .finishDate(task.getFinishDate())
                .status(task.getStatus())
                .project(task.getProject())
                .basicPoints(task.getBasicPoints())
                .penaltyPoints(task.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из сущности в DTO, при обновлении.
     */
    public TaskOutDto taskUpdateToOutDto(Task task) {
        return TaskOutDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .creator(task.getCreator())
                .executor(task.getExecutor())
                .startDate(task.getStartDate())
                .finishDate(task.getFinishDate())
                .project(task.getProject())
                .status(task.getStatus())
                .basicPoints(task.getBasicPoints())
                .penaltyPoints(task.getPenaltyPoints())
                .build();
    }

    /**
     * Преобразование из списка сущностей в список DTO.
     */
    public List<TaskOutDto> tasksToListOutDto(List<Task> listTasks) {
        return listTasks.stream().map(this::taskUpdateToOutDto).toList();
    }
}