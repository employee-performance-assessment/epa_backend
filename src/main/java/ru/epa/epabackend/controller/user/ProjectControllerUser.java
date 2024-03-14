package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.service.ProjectService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ProjectControllerUser содержит ендпоинты, относящиеся к проектам пользователя
 *
 * @author Константин Осипов
 */
@Tag(name = "Private: Проекты", description = "API пользователя для работы с проектами")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/projects")
public class ProjectControllerUser {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

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
    public ProjectShortResponseDto findProject(@PathVariable Long projectId, Principal principal) {
        return projectMapper.mapToShortDto(projectService.findDtoById(projectId, principal.getName()));
    }

    /**
     * Эндпоинт получения списка проектов пользователя с короткой информацией о проектах
     */
    @Operation(
            summary = "Получение списка проектов пользователя с короткой информацией о проектах",
            description = "При успешном получении списка возвращается 200 Ok.\n" +
                    "В случае отсутствия указанного email в базе данных возвращается 404 Not Found."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectShortResponseDto> findByUserEmail(Principal principal) {
        return projectService.findAllByUserEmail(principal.getName()).stream()
                .map(projectMapper::mapToShortDto).collect(Collectors.toList());
    }
}
