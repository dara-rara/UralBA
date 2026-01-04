package ural.ba.project.UralBA.seceruty;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ural.ba.project.UralBA.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
/**
 * Провайдер JWT токенов
 * Отвечает за генерацию Access и Refresh токенов,
 * их валидацию и извлечение содержащихся в них данных (claims)
 * Использует библиотеку JJWT и секретные ключи из конфигурационных файлов
 *
 * @author Daria
 */
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    /**
     * Конструктор для инициализации секретных ключей из параметров приложения
     * Ключи декодируются из формата Base64 и преобразуются в объекты SecretKey
     */
    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.expiration.access}") Duration accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") Duration refreshTokenExpiration
    ) {
        // Добавлено .trim() для устойчивости к случайным пробелам в конфигурации
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret.trim()));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret.trim()));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Генерирует новый JWT Access токен для указанного пользователя
     * Токен подписывается с использованием jwtAccessSecret и содержит email, срок действия и роли пользователя
     */
    public String generateAccessToken(@NotNull User user) {
        final Instant now = Instant.now();
        final Instant accessExpirationInstant = now.plus(accessTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(accessExpirationInstant))
                .signWith(jwtAccessSecret)
                .claim("roles", List.of(user.getRole().getAuthority()))
                .claim("firstName", user.getName())
                .compact();
    }

    /**
     * Генерирует новый JWT Refresh токен для указанного пользователя
     * Токен подписывается с использованием jwtRefreshSecret и имеет больший срок действия
     */
    public String generateRefreshToken(@NotNull User user) {
        final Instant now = Instant.now();
        final Instant refreshExpirationInstant = now.plus(refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(refreshExpirationInstant))
                .signWith(jwtRefreshSecret)
                .claim("roles", List.of(user.getRole().getAuthority()))
                .claim("firstName", user.getName())
                .compact();
    }

    /**
     * Выполняет валидацию предоставленного Access токена
     */
    public boolean validateAccessToken(@NotNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    /**
     * Выполняет валидацию предоставленного Refresh токена
     */
    public boolean validateRefreshToken(@NotNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    /**
     * Метод для валидации токена с использованием заданного секретного ключа
     * Обрабатывает различные исключения JWT (просрочен, невалидная подпись и т.д.)
     */
    private boolean validateToken(@NotNull String token, @NotNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.debug("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.debug("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.debug("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.debug("Invalid signature", sEx);
        } catch (Exception e) {
            log.debug("Invalid token", e);
        }
        return false;
    }

    /**
     * Извлекает тело (Claims) из валидного Access токена
     */
    public Claims getAccessClaims(@NotNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**
     * Извлекает тело (Claims) из валидного Refresh токена
     */
    public Claims getRefreshClaims(@NotNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    /**
     * Приватный вспомогательный метод для парсинга токена и получения его Claims
     */
    private Claims getClaims(@NotNull String token, @NotNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
