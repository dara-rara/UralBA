package ural.ba.project.UralBA.model;

import jakarta.persistence.*;

/**
 * Токин с долгим сроком валидности
 *
 * @author Daria
 */
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_token;

    @Column(nullable = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public RefreshToken(Long idToken, String token, User user) {
        id_token = idToken;
        this.token = token;
        this.user = user;
    }

    public RefreshToken() {

    }

    public Long getId_token() {
        return id_token;
    }

    public void setId_token(Long id_token) {
        this.id_token = id_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
