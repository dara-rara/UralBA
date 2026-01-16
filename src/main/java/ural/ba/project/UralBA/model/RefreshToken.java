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
    private Long idToken;

    @Column(length = 500, nullable = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public RefreshToken(Long idToken, String token, User user) {
        this.idToken = idToken;
        this.token = token;
        this.user = user;
    }

    public RefreshToken() {

    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
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
