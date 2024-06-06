package ru.epa.epabackend.repository;

import java.util.List;

public interface RefreshTokenRepository {
    void saveRefreshToken(String username, String refreshToken);

    List<String> getRefreshTokens(String username);

    boolean existsByUsernameAndRefreshToken(String username, String refreshToken);

    void updateRefreshToken(String username, String oldRefreshToken, String newRefreshToken);
}
