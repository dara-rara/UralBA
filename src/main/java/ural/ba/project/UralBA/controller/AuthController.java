package ural.ba.project.UralBA.controller;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ba.project.UralBA.dto.jwt.JwtRequestDTO;
import ural.ba.project.UralBA.dto.jwt.JwtResponseDTO;
import ural.ba.project.UralBA.dto.jwt.RefreshJwtRequestDTO;
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
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO jwtRequestDTO
    ) throws AuthException {
        JwtResponseDTO response = authService.login(jwtRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт для получения нового Access токена с использованием валидного Refresh токена
     * Не генерирует новый Refresh токен
     */
    @PostMapping("/token")
    public ResponseEntity<JwtResponseDTO> getNewAccessToken(
            @RequestBody RefreshJwtRequestDTO refreshJwtRequestDTO
    ) throws AuthException {
        JwtResponseDTO response = authService.getAccessToken(refreshJwtRequestDTO.refreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт для полного обновления пары Access и Refresh токенов
     * Используется механизм ротации токенов (Refresh Token Rotation)
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> getNewRefreshToken(
            @RequestBody RefreshJwtRequestDTO refreshJwtRequestDTO
    ) throws AuthException {
        JwtResponseDTO response = authService.refresh(refreshJwtRequestDTO.refreshToken());
        return ResponseEntity.ok(response);
    }
}
