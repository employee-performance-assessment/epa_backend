package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Сервис, отвечающий за работу с JWT токенами, включая генерацию, проверку валидности, извлечение имени пользователя
 */
public interface JwtService {
    String extractAccessTokenUserName(String token);

    String extractRefreshTokenUserName(String token);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isAccessTokenValid(String token, UserDetails userDetails);

    boolean validateRefreshToken(String token);
}