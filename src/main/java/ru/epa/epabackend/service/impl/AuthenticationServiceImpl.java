package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.epa.epabackend.dto.employee.JwtRequest;
import ru.epa.epabackend.dto.employee.JwtResponse;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.JwtService;

/**
 * Сервис для генерации JWT токена
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final JwtService jwtService;

    /**
     * Получение JWT токена по запросу.
     *
     * @param jwtRequest
     * @return
     */
    @Override
    public JwtResponse getToken(JwtRequest jwtRequest) {
        log.info("Получение JWT токена по запросу");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        UserDetails userDetails = employeeService.findByEmail(jwtRequest.getEmail());
        return new JwtResponse(jwtService.generateToken(userDetails));
    }
}
