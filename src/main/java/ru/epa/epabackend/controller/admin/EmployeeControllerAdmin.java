package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.service.EmployeeService;

import java.security.Principal;

import static ru.epa.epabackend.util.ValidationGroups.Create;

/**
 * Класс EmployeeControllerAdmin содержит эндпойнты для администратора, относящиеся к сотрудникам.
 *
 * @author Валентина Вахламова
 */
@Tag(name = "Admin: Сотрудники", description = "API для работы с пользователями")
@SecurityRequirement(name = "JWT")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/employee")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт добавления нового сотрудника
     */
    @Operation(
            summary = "Добавление нового сотрудника"
    )
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)

    public EmployeeFullResponseDto addEmployee(
            @Validated(Create.class) @RequestBody @Parameter(required = true)
            EmployeeRequestDto employeeRequestDto, Principal principal) {
        return employeeMapper.mapToFullDto(employeeService.create(employeeRequestDto, principal.getName()));
    }

    /**
     * Эндпойнт удаления сотрудника
     */
    @Operation(
            summary = "Удаление сотрудника",
            description = "Удаляет сотрудника, если он существует в базе данных."
    )
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable @Parameter(required = true) Long employeeId) {
        employeeService.delete(employeeId);
    }
}