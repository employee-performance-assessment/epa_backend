package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.service.project.ProjectService;

import java.util.List;


/**
 * Класс ProjectControllerUser содержит ендпоинты, относящиеся к проектам пользователя
 *
 * @author Константин Осипов
 */
@Tag(name = "Private: Проекты", description = "API пользователя для работы с проектами")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/{userId}/projects")
public class ProjectControllerUser {
    private final ProjectService projectService;

    /**
     * Эндпоинт получения короткой информации о проекте
     */
    @Operation(
            summary = "Получение короткой информации о проекте",
            description = "При успешном получении возвращается 200 Ok\n" +
                    "В случае отсутствия проекта с указанным id возвращается 404 Not Found"
    )
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectShortDto findDtoById(@PathVariable Long projectId) {
        return projectService.findDtoById(projectId);
    }
}
