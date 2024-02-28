package ru.epa.epabackend.controller.task.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.service.task.admin.TaskAdminService;

import java.util.List;

/**
 * Класс TaskAdminController содержит ендпоинты задач для администратора.
 *
 * @author Владислав Осипов
 */
@RestController
@RequestMapping("/admin/tasks")
@RequiredArgsConstructor
@Validated
public class TaskAdminController {

    private final TaskAdminService taskAdminService;

    /**
     * Эндпойнт поиска всех задач.
     */
    @GetMapping
    public List<TaskOutDto> findAllTasks() {
        return taskAdminService.findAll();
    }

    /**
     * Эндпойнт поиска задачи по ID.
     */
    @GetMapping("/{taskId}")
    public TaskOutDto findTaskById(
            @PathVariable Long taskId
    ) {
        return taskAdminService.findById(taskId);
    }

    /**
     * Эндпойнт создания задачи.
     */
    @PostMapping()
    public TaskOutDto createTask(
            @RequestBody TaskInDto taskInDto) {
        return taskAdminService.create(taskInDto);
    }

    /**
     * Эндпойнт обновления задачи.
     */
    @PatchMapping("/{taskId}")
    public TaskOutDto updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskInDto taskInDto) {
        return taskAdminService.update(taskId, taskInDto);
    }

    /**
     * Эндпойнт удаления задачи.
     */
    @DeleteMapping("/{taskId}")
    public void deleteTask(
            @PathVariable Long taskId
    ) {
        taskAdminService.delete(taskId);
    }
}