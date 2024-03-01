package ru.epa.epabackend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeForListDto;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.service.project.ProjectService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{adminId}/projects")
public class ProjectControllerAdmin {
    private final ProjectService projectService;

    @PostMapping
    public ProjectShortDto save(@Valid @RequestBody NewProjectRto newProjectRto) {
        return projectService.save(newProjectRto);
    }

    @PostMapping("/{projectId}/")
    public ProjectEmployeesDto saveWithEmployee(@PathVariable Long projectId,
                                                @RequestParam Long employeeId) {
        return projectService.saveWithEmployee(projectId, employeeId);
    }

    @GetMapping
    public List<ProjectShortDto> findByAdminId(@PathVariable Long adminId) {
        return projectService.findByAdminId(adminId);
    }
}
