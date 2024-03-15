package ru.epa.epabackend.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortRequestDto;
import ru.epa.epabackend.dto.employee.JwtRequest;
import ru.epa.epabackend.dto.employee.JwtResponse;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;

/**
 * Класс PublicController представляет из себя публичный API для получения токена и саморегистрации администратора.
 *
 * @author Егор Яковлев
 */
@Tag(name = "Open: открытые эндпоинты")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {

    private final AuthenticationService authenticationService;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    /**
     * Эндпойнт для получения токена при входе сотрудника
     */
    @Operation(
            summary = "Получение JWT токена по паре логин пароль"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized Error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/auth")
    public JwtResponse getToken(@RequestBody @Parameter(required = true) JwtRequest jwtRequest) {
        return authenticationService.getToken(jwtRequest);
    }

    /**
     * Эндпойнт для саморегистрации администратора
     */
    @Operation(
            summary = "Саморегистрация администратора"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin created", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeFullResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/register")
    public EmployeeFullResponseDto register(@RequestBody EmployeeShortRequestDto employeeShortRequestDto) {
        return employeeMapper.mapToFullDto(employeeService.createSelfRegister(employeeShortRequestDto));
    }
}
