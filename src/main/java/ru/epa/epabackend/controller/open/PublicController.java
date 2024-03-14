package ru.epa.epabackend.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.employee.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;

/**
 *
 */
@Tag(name = "Open: открытые эндпоинты")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {
    private final AuthenticationService authenticationService;
    private final EmployeeService employeeService;

    @Operation(
            summary = "Получение JWT токена по паре логин пароль"
    )
    @PostMapping("/auth")
    public JwtResponse getToken(@RequestBody @Parameter(required = true) JwtRequest jwtRequest) {
        return authenticationService.getToken(jwtRequest);
    }

    @Operation(
            summary = "Саморегистрация администратора"
    )
    @PostMapping("/register")
    public Employee register(@RequestBody EmployeeShortRequestDto employeeShortRequestDto) {
        return employeeService.createSelfRegister(employeeShortRequestDto);
    }
}
