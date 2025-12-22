package ural.ba.project.UralBA.model;

import jakarta.persistence.*;

/**
 * Хранит причины отказа принять пользователя в сообщество
 *
 * @author Daria
 */
@Entity
@Table(name = "reason_refusal")
public class ReasonRefusal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReason;

    @Column(nullable = true)
    private String reason;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public ReasonRefusal(String reason, User user) {
        this.reason = reason;
        this.user = user;
    }

    public ReasonRefusal() {
    }

    public Long getIdReason() {
        return idReason;
    }

    public void setIdReason(Long idReason) {
        this.idReason = idReason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
