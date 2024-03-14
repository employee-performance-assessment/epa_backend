package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.service.EmployeeService;

import java.security.Principal;
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
    private final EmployeeMapper employeeMapper;

    @Operation(
            summary = "Обновление сотрудника"
    )
    @PatchMapping("/{employeeId}")

    public EmployeeFullResponseDto updateEmployee(@PathVariable @Parameter(required = true) Long employeeId,
                                                  @Validated(Update.class) @Parameter(required = true) @RequestBody
                                                  EmployeeRequestDto employeeDtoRequest) {
        return employeeService.update(employeeId, employeeDtoRequest);
    }

    @Operation(
            summary = "Получение всех сотрудников",
            description = "Возвращает список сотрудников в сокращенном виде\n\nВ случае, если не найдено ни одного сотрудника, возвращает пустой список."
    )
    @GetMapping
    public List<EmployeeShortResponseDto> getAllEmployees() {
        return employeeService.findAll();
    }

    @Operation(
            summary = "Получение информации о сотруднике по id",
            description = "Возвращает полную информацию о сотруднике по id, если он существует в базе данных.\n\nВ случае, если сотрудника не найдено , возвращает ошибку 404"
    )
    @GetMapping("/{employeeId}")
    public EmployeeFullResponseDto getEmployeeById(@PathVariable @Parameter(required = true) Long employeeId) {
        return employeeService.findByIdDto(employeeId);
    }

    @Operation(
            summary = "Получение информации о владельце токена",
            description = "Возвращает полную информацию о владельце токена, если он существует в базе данных."
    )
    @GetMapping("/me")
    public EmployeeFullResponseDto getMe(Principal principal) {
        return employeeMapper.mapToFullDto(employeeService.findByEmail(principal.getName()));
    }
}
