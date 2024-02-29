package ru.epa.epabackend.controller.open;

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
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth")
    public JwtResponse getToken(@RequestBody JwtRequest jwtRequest) {
        log.info("POST / jwtRequest / {}", jwtRequest);
        return authenticationService.getToken(jwtRequest);
    }
}