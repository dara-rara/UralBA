package ural.ba.project.UralBA.seceruty;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ural.ba.project.UralBA.model.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Реализация интерфейса {@link Authentication} из Spring Security для хранения
 * информации об аутентифицированном пользователе на основе данных из JWT токена
 *
 * @author Daria
 */
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String username;
    private String firstName;
    private Set<Role> roles;

    /**
     * Получает имя пользователя (обычно email или логин).
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает имя пользователя (персональное имя).
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Устанавливает имя пользователя (персональное имя).
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Получает набор ролей пользователя.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Устанавливает набор ролей пользователя.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Возвращает коллекцию полномочий (ролей) пользователя, требуемых интерфейсом {@link Authentication}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>(roles);
    }

    /**
     * Возвращает учетные данные (пароль), которые в случае JWT токена отсутствуют.
     */
    @Override
    public Object getCredentials() { return null; }

    /**
     * Возвращает дополнительные детали аутентификации.
     */
    @Override
    public Object getDetails() { return null; }

    /**
     * Возвращает главный объект аутентификации (principal), которым является имя пользователя.
     */
    @Override
    public Object getPrincipal() { return username; }

    /**
     * Проверяет, аутентифицирован ли пользователь.
     */
    @Override
    public boolean isAuthenticated() { return authenticated; }

    /**
     * Устанавливает статус аутентификации. Используется фильтром после успешной проверки токена.
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    /**
     * Возвращает имя, связанное с аутентификацией (в данном случае - firstName).
     */
    @Override
    public String getName() { return firstName; }

}
