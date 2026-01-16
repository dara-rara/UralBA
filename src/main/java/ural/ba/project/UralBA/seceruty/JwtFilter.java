package ural.ba.project.UralBA.seceruty;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Фильтр Spring Security, отвечающий за извлечение и валидацию JWT Access токенов
 * из HTTP-запросов
 * Если токен валиден, он устанавливает контекст безопасности Spring
 * В случае невалидного токена использует {@link HandlerExceptionResolver} для обработки ошибки
 * и возврата соответствующего HTTP-статуса
 *
 * @author Daria
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final HandlerExceptionResolver resolver;

    public JwtFilter(JwtProvider jwtProvider,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtProvider = jwtProvider;
        this.resolver = resolver;
    }

    /**
     * Основной метод фильтрации
     * Извлекает токен из заголовка, валидирует его и либо устанавливает Authentication в SecurityContext,
     * либо делегирует обработку ошибки резолверу
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTokenFromRequest(request);

        if (token != null) {
            if (jwtProvider.validateAccessToken(token)) {
                final Claims claims = jwtProvider.getAccessClaims(token);
                final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
                jwtInfoToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
            } else {
                resolver.resolveException(
                        request,
                        response,
                        null,
                        new AuthException("Неправильный или невалидный токен access")
                );
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Вспомогательный метод для извлечения чистой строки токена из заголовка "Authorization: Bearer ...".
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}