package ural.ba.project.UralBA.seceruty;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ural.ba.project.UralBA.model.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daria
 */
@Component
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String username;
    private String firstName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>(roles);
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return username; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return firstName; }

}
