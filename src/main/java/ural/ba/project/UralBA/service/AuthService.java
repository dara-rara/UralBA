package ural.ba.project.UralBA.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.dto.jwt.JwtRequestDTO;
import ural.ba.project.UralBA.dto.jwt.JwtResponseDTO;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.seceruty.JwtAuthentication;
import ural.ba.project.UralBA.seceruty.JwtProvider;

/**
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

    public JwtResponseDTO getAccessToken(String refreshToken) {
        String subRefreshToken = refreshToken.substring(7); // убираем заголовок
        if (jwtProvider.validateRefreshToken(subRefreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(subRefreshToken);
            final String email = claims.getSubject();
            final User user = userService.findByEmail(email);
            final String saveRefreshToken = refreshTokenService.findByUser(user).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(subRefreshToken)) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponseDTO(accessToken, null);
            }
        }
        return new JwtResponseDTO(null, null);
    }

    public JwtResponseDTO refresh(String refreshToken) throws AuthException {
        String subRefreshToken = refreshToken.substring(7); // убираем заголовок
        if (jwtProvider.validateRefreshToken(subRefreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(subRefreshToken);
            final String email = claims.getSubject();
            final User user = userService.findByEmail(email);
            final String saveRefreshToken = refreshTokenService.findByUser(user).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(subRefreshToken)) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                RefreshToken refreshTokenEntity = refreshTokenService.findByUser(user);
                refreshTokenEntity.setToken(newRefreshToken);
                refreshTokenService.save(refreshTokenEntity);
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT refresh токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}