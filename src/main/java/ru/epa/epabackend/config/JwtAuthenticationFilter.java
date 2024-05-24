package ru.epa.epabackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.epa.epabackend.exception.ErrorResponse;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.JwtService;

import java.io.IOException;

/**
 * Фильтр Spring security для проверки входящего запроса.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final EmployeeService employeeService;
    private final ObjectMapper mapper;

    /**
     * Если запрос не содержит заголовок авторизации, то он идет дальше по цепочке.
     * Если запрос содержит заголовок авторизации, то он проверяется.
     * Если токен валидный, то пользователь добавляется в контекст секьюрити.
     * Иначе возвращается {@link  HttpStatus#UNAUTHORIZED}
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userName;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userName = jwtService.extractUserName(jwt);
            if (!userName.isEmpty()
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = employeeService.findByEmail(userName);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn(e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, "Срок вашей сессии завершён. Авторизуйтесь снова")));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (SignatureException | MalformedJwtException | StringIndexOutOfBoundsException e) {
            log.warn(e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(mapper.writeValueAsString(
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage())));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }
}
