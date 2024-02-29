package ru.epa.epabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.service.task.TaskService;

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
public class TaskControllerAdmin {

    private final TaskService taskService;

    /**
     * Эндпойнт поиска всех задач.
     */
    @GetMapping
    public List<TaskShortDto> findAllTasks() {
        return taskService.findAllByAdmin();
    }

    /**
     * Эндпойнт поиска задачи по ID.
     */
    @GetMapping("/{taskId}")
    public TaskFullDto findTaskById(@PathVariable Long taskId) {
        return taskService.findByIdByAdmin(taskId);
    }

    /**
     * Эндпойнт создания задачи.
     */
    @PostMapping()
    public TaskFullDto createTask(@RequestBody TaskInDto taskInDto) {
        return taskService.createByAdmin(taskInDto);
    }

    /**
     * Эндпойнт обновления задачи.
     */
    @PatchMapping("/{taskId}")
    public TaskFullDto updateTask(@PathVariable Long taskId, @RequestBody TaskInDto taskInDto) {
        return taskService.updateByAdmin(taskId, taskInDto);
    }

    /**
     * Эндпойнт удаления задачи.
     */
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteByAdmin(taskId);
    }
}