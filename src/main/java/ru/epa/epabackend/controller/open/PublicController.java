package ru.epa.epabackend.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeFullDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.dto.employee.RequestJwt;
import ru.epa.epabackend.dto.employee.ResponseJwt;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.ValidationGroups.Create;

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
    @Operation(summary = "Получение JWT токена по паре логин пароль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/auth")
    public ResponseJwt getToken(@Valid @RequestBody @Parameter(required = true) RequestJwt requestJwt) {
        return authenticationService.getToken(requestJwt);
    }

    /**
     * Эндпойнт для саморегистрации администратора
     */
    @Operation(summary = "Саморегистрация администратора")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ResponseEmployeeFullDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEmployeeFullDto register(@Validated(Create.class) @RequestBody RequestEmployeeShortDto requestEmployeeShortDto) {
        return employeeMapper.mapToFullDto(employeeService.createSelfRegister(requestEmployeeShortDto));
    }
}