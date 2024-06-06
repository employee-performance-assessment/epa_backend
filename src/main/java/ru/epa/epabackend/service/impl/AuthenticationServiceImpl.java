package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.RequestJwt;
import ru.epa.epabackend.dto.employee.ResponseJwt;
import ru.epa.epabackend.exception.exceptions.UnauthorizedException;
import ru.epa.epabackend.repository.RefreshTokenRepository;
import ru.epa.epabackend.service.AuthenticationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.JwtService;

/**
 * Сервис для генерации токенов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Получение access и refresh токенов по запросу.
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseJwt getTokens(RequestJwt requestJwt) {
        log.info("Получение access и refresh токенов по запросу");
        requestJwt.setEmail(requestJwt.getEmail().toLowerCase());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestJwt.getEmail(), requestJwt.getPassword()));
        UserDetails userDetails = employeeService.findByEmail(requestJwt.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        refreshTokenRepository.saveRefreshToken(userDetails.getUsername(), refreshToken);
        return new ResponseJwt(accessToken, refreshToken);
    }

    /**
     * Обновляем пару токенов на основе валидного токена обновления
     */
    @Override
    public ResponseJwt refresh(String refreshToken) {
        jwtService.validateRefreshToken(refreshToken);
        String username = jwtService.extractRefreshTokenUserName(refreshToken);
        if (!refreshTokenRepository.existsByUsernameAndRefreshToken(username, refreshToken)) {
            throw new UnauthorizedException("Токен обновления не найден");
        }
        UserDetails userDetails = employeeService.findByEmail(username);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        refreshTokenRepository.updateRefreshToken(username, refreshToken, newRefreshToken);
        return new ResponseJwt(accessToken, newRefreshToken);
    }
}
