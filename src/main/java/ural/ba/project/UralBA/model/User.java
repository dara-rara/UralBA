package ural.ba.project.UralBA.model;

import jakarta.persistence.*;

/**
 * Пользователь
 *
 * @author Daria
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User(Long id_user, String name, String email, String password, String position, Role role) {
        this.id_user = id_user;
        this.name = name;
        this.email = email;
        this.password = password;
        this.position = position;
        this.role = role;
    }

    public User() {

    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(nullable = false)
    private String position;

    @Enumerated(EnumType.STRING)
    private Role role;

}
