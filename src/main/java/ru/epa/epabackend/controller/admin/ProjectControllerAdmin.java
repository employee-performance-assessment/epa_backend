package ru.epa.epabackend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{adminId}/projects")
public class ProjectControllerAdmin {
    private final ProjectService projectService;

    /**
     * Эндпоинт сохранения информации о проекте
     */
    @PostMapping
    public ProjectShortDto save(@Valid @RequestBody NewProjectRto newProjectRto, @PathVariable Long adminId) {
        return projectService.save(newProjectRto, adminId);
    }

    /**
     * Эндпоинт добавления сотрудника в проект
     */
    @PostMapping("/{projectId}/")
    public ProjectEmployeesDto saveWithEmployee(@PathVariable Long projectId,
                                                @RequestParam Long employeeId) {
        return projectService.saveWithEmployee(projectId, employeeId);
    }

    /**
     * Эндпоинт получения списка проектов администратора с короткой информацией о них
     */
    @GetMapping
    public List<ProjectShortDto> findByAdminId(@PathVariable Long adminId) {
        return projectService.findByAdminId(adminId);
    }

    /**
     * Эндпоинт получения списка сотрудников, участвующих в проекте
     */
    @GetMapping("/{projectId}")
    public List<EmployeeForListDto> findByProjectIdAndRole(@PathVariable Long projectId) {
        return projectService.findByProjectIdAndRole(projectId, Role.ROLE_USER);
    }

    /**
     * Эндпоинт изменения информации о проекте
     */
    @PatchMapping("/{projectId}")
    public ProjectShortDto update(@PathVariable Long projectId, @Valid @RequestBody UpdateProjectRto updateProjectRto) {
        return projectService.update(projectId, updateProjectRto);
    }

    /**
     * Эндпоинт удаления проекта
     */
    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }

    /**
     * Эндпоинт удаления сотрудника из проекта
     */
    @DeleteMapping("/{projectId}/{employeeId}")
    public void deleteEmployeeFromProject(@PathVariable Long projectId,
                                          @PathVariable Long employeeId) {
        projectService.deleteEmployeeFromProject(projectId, employeeId);
    }
}