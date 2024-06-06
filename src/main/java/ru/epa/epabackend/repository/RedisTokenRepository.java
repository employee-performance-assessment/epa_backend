package ru.epa.epabackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository implements RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REFRESH_TOKENS_PER_USER = 5;

    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        List<String> refreshTokens = getRefreshTokens(username);
        if (refreshTokens.size() >= MAX_REFRESH_TOKENS_PER_USER) {
            redisTemplate.opsForList().leftPop(username);
        }
        redisTemplate.opsForList().rightPush(username, refreshToken);
    }

    @Override
    public List<String> getRefreshTokens(String username) {
        return redisTemplate.opsForList().range(username, 0, -1);
    }

    @Override
    public boolean existsByUsernameAndRefreshToken(String username, String refreshToken) {
        List<String> refreshTokens = redisTemplate.opsForList().range(username, 0, -1);
        return refreshTokens != null && refreshTokens.contains(refreshToken);
    }

    @Override
    public void updateRefreshToken(String username, String oldRefreshToken, String newRefreshToken) {
        deleteRefreshToken(username, oldRefreshToken);
        saveRefreshToken(username, newRefreshToken);
    }

    private void deleteRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForList().remove(username, 0, refreshToken);
    }
}
