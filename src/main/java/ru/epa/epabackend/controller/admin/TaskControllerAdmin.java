package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskCreateFindByIdUpdateResponseDto;
import ru.epa.epabackend.dto.task.TaskCreateUpdateRequestDto;
import ru.epa.epabackend.dto.task.TaskFindAllResponseDto;
import ru.epa.epabackend.service.TaskService;

import java.util.List;

/**
 * Класс TaskAdminController содержит ендпоинты задач для администратора.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("/admin/tasks")
@RequiredArgsConstructor
@Validated
public class TaskControllerAdmin {

    private final TaskService taskService;

    /**
     * Эндпойнт поиска всех задач администратором.
     */
    @Operation(
            summary = "Получение всех задач администратором",
            description = "Возвращает список задач в сокращенном виде. " +
                    "В случае, если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
    public List<TaskFindAllResponseDto> findAllByAdmin() {
        return taskService.findAll();
    }

    /**
     * Эндпойнт поиска задачи по ID администратором.
     */
    @Operation(
            summary = "Получение информации о задаче администратором",
            description = "Возвращает полную информацию о задаче, если она существует в базе данных. " +
                    "В случае, если задачи не найдено, возвращает ошибкую 404"
    )
    @GetMapping("/{taskId}")
    public TaskCreateFindByIdUpdateResponseDto findByIdByAdmin(
            @Parameter(required = true) @PathVariable Long taskId) {
        return taskService.findDtoById(taskId);
    }

    /**
     * Эндпойнт создания задачи.
     */
    @Operation(
            summary = "Добавление новой задачи администратором",
            description = "Создание новой задачи администратором"
    )
    @PostMapping()
    public TaskCreateFindByIdUpdateResponseDto createByAdmin(
            @Parameter(required = true) @RequestBody TaskCreateUpdateRequestDto taskInDto) {
        return taskService.create(taskInDto);
    }

    /**
     * Эндпойнт обновления задачи.
     */
    @Operation(
            summary = "Обновление задачи администратором",
            description = "Обновление задачи администратором"
    )
    @PatchMapping("/{taskId}")
    public TaskCreateFindByIdUpdateResponseDto updateByAdmin(@Parameter(required = true) @PathVariable Long taskId,
                                     @Parameter(required = true) @RequestBody TaskCreateUpdateRequestDto taskInDto) {
        return taskService.update(taskId, taskInDto);
    }

    /**
     * Эндпойнт удаления задачи.
     */
    @Operation(
            summary = "Удаление задачи администратором",
            description = "Удаляет задачу, если она существует в базе данных."
    )
    @DeleteMapping("/{taskId}")
    public void deleteByAdmin(@Parameter(required = true) @PathVariable Long taskId) {
        taskService.delete(taskId);
    }
}