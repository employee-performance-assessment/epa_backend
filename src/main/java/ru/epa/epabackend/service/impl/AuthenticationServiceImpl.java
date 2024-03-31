package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.epa.epabackend.dto.employee.RequestJwt;
import ru.epa.epabackend.dto.employee.ResponseJwt;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.JwtService;

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
     *
     * @param requestJwt
     * @return
     */
    @Override
    public ResponseJwt getToken(RequestJwt requestJwt) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestJwt.getEmail(), requestJwt.getPassword()));
        UserDetails userDetails = employeeService.findByEmail(requestJwt.getEmail());
        return new ResponseJwt(jwtService.generateToken(userDetails));
    }
}
