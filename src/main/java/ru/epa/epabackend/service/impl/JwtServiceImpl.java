package ru.epa.epabackend.service.impl;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.epa.epabackend.exception.exceptions.UnauthorizedException;
import ru.epa.epabackend.service.JwtService;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Имплементация {@link JwtService}
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    /**
     * Секрет, для подписания токена. Рекомендуется использовать 256-bit WEP ключ, который указывается в настройках.
     * Пример сервиса для генерации ключей можно использовать сервис <a href="https://randomkeygen.com/">https://randomkeygen.com/</a>
     */
    @Value("${token.secret.access}")
    private String jwtAccessSecret;

    @Value("${token.secret.refresh}")
    private String jwtRefreshSecret;

    /**
     * Срок действия токена доступа.
     */
    @Value("${token.duration.access}")
    private Duration tokenDurationAccess;

    /**
     * Срок действия токена обновления.
     */
    @Value("${token.duration.refresh}")
    private Duration tokenDurationRefresh;

    /**
     * Извлечение логина из токена доступа.
     */
    @Override
    public String extractAccessTokenUserName(String token) {
        log.info("Извлечение логина из токена");
        return extractAccessTokenClaim(token, Claims::getSubject);
    }

    /**
     * Извлечение логина из токена обновления.
     */
    @Override
    public String extractRefreshTokenUserName(String token) {
        log.info("Извлечение логина из токена");
        return extractRefreshTokenClaim(token, Claims::getSubject);
    }

    /**
     * Генерация токена доступа на основании данных пользователя.
     */
    @Override
    public String generateAccessToken(UserDetails userDetails) {
        log.info("Генерация токена доступа на основании данных пользователя");
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + tokenDurationAccess.toMillis());

        return Jwts.builder()
                .setClaims(generateClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, jwtAccessSecret)
                .compact();
    }

    /**
     * Генерация токена обновления на основании данных пользователя.
     */
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        log.info("Генерация токена обновления на основании данных пользователя");
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + tokenDurationRefresh.toMillis());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, jwtRefreshSecret)
                .compact();
    }

    /**
     * Проверка срока токена доступа
     */
    @Override
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        log.info("Проверка срока токена доступа");
        final String userName = extractAccessTokenUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtRefreshSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Токен обновления истек");
        } catch (SignatureException e) {
            throw new UnauthorizedException("Недействительная подпись токена обновления");
        } catch (Exception e) {
            throw new UnauthorizedException("Недействительный токен обновления");
        }
    }

    private <T> T extractAccessTokenClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = getAccessClaims(token);
        return claimsResolvers.apply(claims);
    }

    private <T> T extractRefreshTokenClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = getRefreshClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Map<String, Object> generateClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        return claims;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAccessTokenClaim(token, Claims::getExpiration);
    }

    private Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    private Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}