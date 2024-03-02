package ru.epa.epabackend.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.service.project.ProjectService;

import java.util.List;


/**
 * Класс ProjectControllerUser содержит ендпоинты, относящиеся к проектам пользователя
 *
 * @author Константин Осипов
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/{userId}/projects")
public class ProjectControllerUser {
    private final ProjectService projectService;

    /**
     * Эндпоинт получения информации о проекте
     */
    @GetMapping("/{projectId}")
    public ProjectShortDto findDtoById(@PathVariable Long projectId) {
        return projectService.findDtoById(projectId);
    }
}
