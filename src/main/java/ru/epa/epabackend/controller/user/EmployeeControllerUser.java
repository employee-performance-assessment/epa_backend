package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.service.EmployeeService;

import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Update;

@Tag(name = "Private: Сотрудники", description = "Закрытый API для работы с пользователями")
@SecurityRequirement(name = "JWT")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/employees")
public class EmployeeControllerUser {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Обновление сотрудника"
    )
    @PatchMapping("/{employeeId}")

    public EmployeeFullDto updateEmployee(@PathVariable @Parameter(required = true) Long employeeId,
                                          @Validated(Update.class) @Parameter(required = true) @RequestBody
                                          EmployeeDtoRequest employeeDtoRequest) {
        log.info("PATCH / employees / {}", employeeId);
        return employeeService.updateEmployee(employeeId, employeeDtoRequest);
    }

    @Operation(
            summary = "Получение всех сотрудников",
            description = "Возвращает список сотрудников в сокращенном виде\n\nВ случае, если не найдено ни одного сотрудника, возвращает пустой список."
    )
    @GetMapping
    public List<EmployeeShortDto> getAllEmployees() {
        log.info("GET / employees");
        return employeeService.getAllEmployees();
    }

    @Operation(
            summary = "Получение информации о сотруднике",
            description = "Возвращает полную информацию о сотруднике, если он существует в базе данных.\n\nВ случае, если сотрудника не найдено , возвращает ошибкую 404"
    )
    @GetMapping("/{employeeId}")
    public EmployeeFullDto getEmployeeById(@PathVariable @Parameter(required = true) Long employeeId) {
        log.info("GET / employees / {}", employeeId);
        return employeeService.getEmployeeById(employeeId);
    }
}
