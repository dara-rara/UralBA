package ural.ba.project.UralBA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.User;

import java.util.Optional;

/**
 * @author Daria
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUser(User user);
}