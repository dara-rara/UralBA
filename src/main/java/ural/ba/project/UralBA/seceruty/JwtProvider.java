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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author Daria
 */
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NotNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", List.of(user.getRole().getAuthority()))
                .claim("firstName", user.getName())
                .compact();
    }

    public String generateRefreshToken(@NotNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .claim("roles", List.of(user.getRole().getAuthority()))
                .claim("firstName", user.getName())
                .compact();
    }

    public boolean validateAccessToken(@NotNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NotNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NotNull String token, @NotNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NotNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NotNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NotNull String token, @NotNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}