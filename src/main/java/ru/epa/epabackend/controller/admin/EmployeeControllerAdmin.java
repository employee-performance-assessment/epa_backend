package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.service.EmployeeService;

import static ru.epa.epabackend.util.ValidationGroups.Create;

@Tag(name = "Admin: Сотрудники", description = "API для работы с пользователями")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/employees")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Добавление нового сотрудника"
    )
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeDtoResponseFull addEmployee(@Validated(Create.class) @RequestBody @Parameter(required = true)
                                                   EmployeeRtoRequest employeeRtoRequest) {
        log.info("POST / employees / {} ", employeeRtoRequest.getFullName());
        return employeeService.addEmployee(employeeRtoRequest);
    }

    @Operation(
            summary = "Удаление сотрудника",
            description = "Удаляет сотрудника, если он существует в базе данных."
    )
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable @Parameter(required = true) Long employeeId) {
        log.info("DELETE / employees / {}", employeeId);
        employeeService.deleteEmployee(employeeId);
    }
}