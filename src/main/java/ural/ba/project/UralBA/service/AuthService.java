package ural.ba.project.UralBA.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.dto.jwt.JwtRequestDTO;
import ural.ba.project.UralBA.dto.jwt.JwtResponseDTO;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.seceruty.JwtProvider;

/**
 * Сервисный класс, реализующий бизнес-логику аутентификации,
 * управления токенами входа и обновления сессий пользователей.
 *
 * @author Daria
 */
@Service
public class AuthService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, RefreshTokenService refreshTokenService,
                       JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Выполняет аутентификацию пользователя по email и паролю
     * В случае успеха генерирует и сохраняет новые Access и Refresh токены
     *
     * @throws AuthException Если пароль не совпадает
     */
    public JwtResponseDTO login(JwtRequestDTO jwtRequestDTO) throws AuthException {
        final User user = userService.findByEmail(jwtRequestDTO.email());
        if (passwordEncoder.matches(jwtRequestDTO.password(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            RefreshToken refreshTokenEntity = refreshTokenService.findByUser(user);
            refreshTokenEntity.setToken(refreshToken);
            refreshTokenService.save(refreshTokenEntity);
            return new JwtResponseDTO(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    /**
     * Предоставляет новый Access токен на основе валидного Refresh токена
     * Проверяет валидность токена и его совпадение с токеном, сохраненным в базе данных
     * Refresh токен при этом не обновляется
     *
     * @throws AuthException Если токен невалиден или не совпадает с данными в БД
     */
    public JwtResponseDTO getAccessToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final User user = userService.findByEmail(email);
            final String saveRefreshToken = refreshTokenService.findByUser(user).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponseDTO(accessToken, null);
            }
        }
        throw new AuthException("Указан неверный токен refresh или срок действия сеанса истёк");
    }

    /**
     * Полностью обновляет пару Access и Refresh токенов (ротация токенов)
     * Проверяет валидность входящего Refresh токена, генерирует новые токены
     *
     * @throws AuthException Если токен невалиден или не совпадает с данными в БД.
     */
    public JwtResponseDTO refresh(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final User user = userService.findByEmail(email);
            final String saveRefreshToken = refreshTokenService.findByUser(user).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                RefreshToken refreshTokenEntity = refreshTokenService.findByUser(user);
                refreshTokenEntity.setToken(newRefreshToken);
                refreshTokenService.save(refreshTokenEntity);
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Указан неверный токен refresh или срок действия сеанса истёк");
    }

}
