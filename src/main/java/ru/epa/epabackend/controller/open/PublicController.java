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
import ru.epa.epabackend.dto.employee.JwtRequest;
import ru.epa.epabackend.dto.employee.JwtResponse;
import ru.epa.epabackend.service.AuthenticationService;

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

    @Operation(
            summary = "Получение JWT токена по паре логин пароль"
    )
    @PostMapping("/auth")
    public JwtResponse getToken(@RequestBody @Parameter(required = true) JwtRequest jwtRequest) {
        log.info("POST / jwtRequest / {}", jwtRequest);
        return authenticationService.getToken(jwtRequest);
    }
}