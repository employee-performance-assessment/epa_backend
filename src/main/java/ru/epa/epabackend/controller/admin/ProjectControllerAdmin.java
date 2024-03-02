package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeForListDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.project.UpdateProjectRto;
import ru.epa.epabackend.service.project.ProjectService;
import ru.epa.epabackend.util.Role;

import java.util.List;

/**
 * Класс ProjectControllerAdmin содержит ендпоинты, относящиеся к проектам администратора
 *
 * @author Константин Осипов
 */
@Tag(name = "Admin: Проекты", description = "API администратора для работы с проектами")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{adminId}/projects")
public class ProjectControllerAdmin {
    private final ProjectService projectService;

    /**
     * Эндпоинт добавления нового проекта
     */
    @Operation(
            summary = "Добавление нового проекта",
            description = "При успешном добавлении возвращается код 201 Created"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectShortDto save(@Valid @RequestBody NewProjectRto newProjectRto, @PathVariable Long adminId) {
        return projectService.save(newProjectRto, adminId);
    }

    /**
     * Эндпоинт добавления сотрудника в проект
     */
    @Operation(
            summary = "Добавление сотрудника в проект",
            description = "Добавляет сотрудника в проект, если сотрудник и проект существуют.\n" +
                    "При успешном добавлении возвращается код 201 Created\n " +
                    "В случае отсутствия сотрудника или проекта возвращается ошибка 404 Not Found\n" +
                    "Если сотрудник был добавлен в проект ранее, возвращается ошибка 409 Conflict"
    )
    @PostMapping("/{projectId}/")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectEmployeesDto saveWithEmployee(@PathVariable Long projectId,
                                                @RequestParam Long employeeId) {
        return projectService.saveWithEmployee(projectId, employeeId);
    }

    /**
     * Эндпоинт получения списка проектов администратора с короткой информацией о проектах
     */
    @Operation(
            summary = "Получение списка проектов администратора с короткой информацией о проектах",
            description = "При успешном получении списка возвращается 200 Ok"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectShortDto> findByAdminId(@PathVariable Long adminId) {
        return projectService.findByAdminId(adminId);
    }

    /**
     * Эндпоинт получения списка сотрудников, участвующих в проекте
     */
    @Operation(
            summary = "Получение списка сотрудников, участвующих в проекте",
            description = "При успешном получении списка сотрудников проекта возвращается 200 Ok.\n" +
                    "В случае отсутствия проекта возвращается 404 Not Found"
    )
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeForListDto> findByProjectIdAndRole(@PathVariable Long projectId) {
        return projectService.findByProjectIdAndRole(projectId, Role.ROLE_USER);
    }

    /**
     * Эндпоинт изменения информации о проекте
     */
    @Operation(
            summary = "Изменение информации о проекте",
            description = "При успешном изменении данных проекта вернётся 200 OK\n" +
                    "Некорректная величины входных данных вернут 400 Bad Request\n" +
                    "В случае отсутствия проекта с указанным id вернётся ошибка 404 Not Found"
    )
    @PatchMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectShortDto update(@PathVariable Long projectId, @Valid @RequestBody UpdateProjectRto updateProjectRto) {
        return projectService.update(projectId, updateProjectRto);
    }

    /**
     * Эндпоинт удаления проекта
     */
    @Operation(
            summary = "Удаление проекта",
            description = "При успешном удалении проекта 204 No Content\n" +
                    "В случае отсутствия проекта с указанным id возвращается 404 Not Found"
    )
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }

    /**
     * Эндпоинт удаления сотрудника из проекта
     */
    @Operation(
            summary = "Удаление сотрудника из проекта",
            description = "При успешном удалении - 204 No Content\n" +
                    "Если сотрудника или проекта с указанными id не существует возвращается 404 Not Found\n" +
                    "В случае, если сотрудник не состоял в проекте возвращается 409 Conflict"
    )
    @DeleteMapping("/{projectId}/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeFromProject(@PathVariable Long projectId,
                                          @PathVariable Long employeeId) {
        projectService.deleteEmployeeFromProject(projectId, employeeId);
    }
}