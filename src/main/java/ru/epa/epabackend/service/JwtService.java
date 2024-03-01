package ru.epa.epabackend.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Сервис, отвечающий за работу с JWT токенами, включая генерацию, проверку валидности, извлечение имени пользователя.
 */
public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}