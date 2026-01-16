package ural.ba.project.UralBA.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Роли пользователя
 *
 * @author Daria
 */
public enum Role implements GrantedAuthority {

    /**
     * Новая заявка
     */
    NEW_BID("NEW_BID"),
    /**
     * Отклоненная заявка
     */
    REJECTED_BID("REJECTED_BID"),
    /**
     * Принятый пользователь по заявке
     */
    USER("USER"),
    /**
     * Админ
     */
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }


    @Override
    public String getAuthority() {
        return role;
    }

}