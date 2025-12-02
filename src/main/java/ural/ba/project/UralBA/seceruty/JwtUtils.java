package ural.ba.project.UralBA.seceruty;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import ural.ba.project.UralBA.model.Role;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Утилитарный класс, содержащий статические методы для преобразования
 * данных (claims) из JWT токена в объект аутентификации Spring Security ({@link JwtAuthentication})
 *
 * @author Daria
 */
@Component
public final class JwtUtils {

    /**
     * Генерирует объект {@link JwtAuthentication} из набора claims JWT токена
     */
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    /**
     * Извлекает роли пользователя из claims и преобразует их в набор перечислений {@link Role}.
     */
    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}