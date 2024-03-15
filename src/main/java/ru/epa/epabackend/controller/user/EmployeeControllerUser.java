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
import java.util.stream.Collectors;

import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс EmployeeControllerUser содержит эндпойнты для атворизованного пользователя, относящиеся к сотрудникам.
 *
 * @author Валентина Вахламова
 */
@Tag(name = "Private: Сотрудники", description = "Закрытый API для работы с пользователями")
@SecurityRequirement(name = "JWT")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/employees")
public class EmployeeControllerUser {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт по обновлению существующего сотрудника
     */
    @Operation(
            summary = "Обновление сотрудника"
    )
    @PatchMapping("/{employeeId}")

    public EmployeeFullResponseDto updateEmployee(@PathVariable @Parameter(required = true) Long employeeId,
                                                  @Validated(Update.class) @Parameter(required = true) @RequestBody
                                                  EmployeeRequestDto employeeDtoRequest) {
        return employeeMapper.mapToFullDto(employeeService.update(employeeId, employeeDtoRequest));
    }

    /**
     * Эндпойнт получения всех сотрудников в скоращенном виде
     */
    @Operation(
            summary = "Получение всех сотрудников",
            description = "Возвращает список сотрудников в сокращенном виде\n\nВ случае, если не найдено ни одного сотрудника, возвращает пустой список."
    )
    @GetMapping
    public List<EmployeeShortResponseDto> findAll() {
        return employeeService.findAll().stream().map(employeeMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    /**
     * Эндпойнт получения полных данных о сотрднике по id
     */
    @Operation(
            summary = "Получение информации о сотруднике по id",
            description = "Возвращает полную информацию о сотруднике по id, если он существует в базе данных.\n\nВ случае, если сотрудника не найдено , возвращает ошибку 404"
    )
    @GetMapping("/{employeeId}")
    public EmployeeFullResponseDto findByIdDto(@PathVariable @Parameter(required = true) Long employeeId) {
        return employeeMapper.mapToFullDto(employeeService.findByIdDto(employeeId));
    }

    /**
     * Эндпойнт получения полных данных о сотрднике по токену
     */
    @Operation(
            summary = "Получение информации о владельце токена",
            description = "Возвращает полную информацию о владельце токена, если он существует в базе данных."
    )
    @GetMapping("/me")
    public EmployeeFullResponseDto getMe(Principal principal) {
        return employeeMapper.mapToFullDto(employeeService.findByEmail(principal.getName()));
    }
}
