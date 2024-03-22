package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.service.EmployeeService;

import java.security.Principal;
import java.util.List;

/**
 * Класс EmployeeControllerUser содержит эндпойнты для атворизованного пользователя, относящиеся к сотрудникам.
 *
 * @author Валентина Вахламова
 */
@Tag(name = "Private: Сотрудники", description = "Закрытый API для работы с пользователями")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/employee")
public class EmployeeControllerUser {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт получения всех сотрудников в сокращенном виде
     */
    @Operation(
            summary = "Получение всех сотрудников",
            description = "Возвращает список сотрудников в сокращенном виде\n\n" +
                    "В случае, если не найдено ни одного сотрудника, возвращает пустой список."
    )
    @GetMapping
    public List<EmployeeShortResponseDto> findAll() {
        List<Employee> employees = employeeService.findAll();
        return employeeMapper.mapList(employees);
    }

    /**
     * Эндпойнт получения полных данных о сотруднике по id
     */
    @Operation(
            summary = "Получение информации о сотруднике по id",
            description = "Возвращает полную информацию о сотруднике по id, если он существует в базе данных.\n\n" +
                    "В случае, если сотрудника не найдено , возвращает ошибку 404"
    )
    @GetMapping("/{employeeId}")
    public EmployeeFullResponseDto findByIdDto(@PathVariable @Parameter(required = true) Long employeeId) {
        return employeeMapper.mapToFullDto(employeeService.findByIdDto(employeeId));
    }

    /**
     * Эндпойнт получения полных данных о сотруднике по токену
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
