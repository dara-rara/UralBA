package ural.ba.project.UralBA.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ural.ba.project.UralBA.dto.jwt.JwtRequestDTO;
import ural.ba.project.UralBA.dto.jwt.JwtResponseDTO;
import ural.ba.project.UralBA.service.AuthService;

/**
 * Контроллер для обработки запросов аутентификации и управления токенами (вход в систему,
 * обновление Access и Refresh токенов)
 *
 * @author Daria
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Эндпоинт для входа пользователя в систему (логин)
     */
    @Transactional
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @RequestBody JwtRequestDTO jwtRequestDTO, HttpServletResponse response
    ) throws AuthException {
        JwtResponseDTO jwtResponse = authService.login(jwtRequestDTO);
        setRefreshTokenCookie(response, jwtResponse.getRefreshToken());
        return ResponseEntity.ok(new JwtResponseDTO(jwtResponse.getAccessToken(), null));
    }

    /**
     * Эндпоинт для получения нового Access токена с использованием валидного Refresh токена
     * Refresh token берется из cookies
     */
    @Transactional(readOnly = true)
    @GetMapping("/token")
    public ResponseEntity<JwtResponseDTO> getNewAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletRequest request) throws AuthException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            // Альтернативно можно попробовать получить из заголовка
            refreshToken = request.getHeader("X-Refresh-Token");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthException("Refresh token не передан на сервер");
        }
        JwtResponseDTO jwtResponse = authService.getAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtResponseDTO(jwtResponse.getAccessToken(), null));
    }

    /**
     * Эндпоинт для выхода из системы
     */
    @Transactional
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) throws AuthException {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            authService.logout(refreshToken);
        }
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    /**
     * Устанавливает refresh token в HTTP-only cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // true для production (HTTPS)
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60) // 7 дней
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * Очищает refresh token cookie
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
