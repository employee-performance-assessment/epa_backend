package ru.epa.epabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.epa.epabackend.dto.employee.JwtRequest;
import ru.epa.epabackend.dto.employee.JwtResponse;

/**
 * Сервис для генерации JWT токена
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final JwtService jwtService;

    /**
     * Получение JWT токена по запросу.
     * @param jwtRequest
     * @return
     */
    @Override
    public JwtResponse getToken(JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getLogin(), jwtRequest.getPassword()));
        UserDetails userDetails = employeeService.getEmployeeByLogin(jwtRequest.getLogin());
        return new JwtResponse(jwtService.generateToken(userDetails));
    }
}
