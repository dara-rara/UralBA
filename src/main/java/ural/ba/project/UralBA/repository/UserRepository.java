package ural.ba.project.UralBA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Daria
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}