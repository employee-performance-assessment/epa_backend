package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.task.TaskFullResponseDto;
import ru.epa.epabackend.dto.task.TaskRequestDto;
import ru.epa.epabackend.dto.task.TaskShortResponseDto;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.service.TaskService;

import java.security.Principal;
import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс TaskControllerAdmin содержит эндпойнты для администратора, относящиеся к задачам.
 *
 * @author Владислав Осипов
 */
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Задачи", description = "Закрытый API для работы с задачами")
@RestController
@RequestMapping("/admin/task")
@RequiredArgsConstructor
@Validated
public class TaskControllerAdmin {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Эндпойнт поиска всех задач администратором.
     */
    @Operation(
            summary = "Получение всех задач администратором",
            description = "Возвращает список задач в сокращенном виде. " +
                    "В случае, если не найдено ни одной задачи, возвращает пустой список."
    )
    @GetMapping
    public List<TaskShortResponseDto> findAllByAdmin(Principal principal) {
        return taskMapper.mapList(taskService.findAll(principal.getName()));
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
    public TaskFullResponseDto findByIdByAdmin(@Parameter(required = true) @PathVariable Long taskId,
                                               Principal principal) {
        return taskMapper.mapToFullDto(taskService.findDtoById(taskId, principal.getName()));
    }

    /**
     * Эндпойнт создания задачи.
     */
    @Operation(
            summary = "Добавление новой задачи администратором",
            description = "Создание новой задачи администратором"
    )
    @PostMapping()
    public TaskFullResponseDto createByAdmin(@Validated(Create.class) @Parameter(required = true)
                                             @RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.create(taskRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт обновления задачи.
     */
    @Operation(
            summary = "Обновление задачи администратором",
            description = "Обновление задачи администратором"
    )
    @PatchMapping("/{taskId}")
    public TaskFullResponseDto updateByAdmin(@Parameter(required = true) @PathVariable Long taskId,
                                             @Validated(Update.class) @Parameter(required = true)
                                             @RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return taskMapper.mapToFullDto(taskService.update(taskId, taskRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт удаления задачи.
     */
    @Operation(
            summary = "Удаление задачи администратором",
            description = "Удаляет задачу, если она существует в базе данных."
    )
    @DeleteMapping("/{taskId}")
    public void deleteByAdmin(@Parameter(required = true) @PathVariable Long taskId, Principal principal) {
        taskService.delete(taskId, principal.getName());
    }
}