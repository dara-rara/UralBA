package ural.ba.project.UralBA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:nameFilter IS NULL OR :nameFilter = '' OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT(:nameFilter, '%')))")
    List<User> findByRoleAndFilter(
            @Param("role") Role role,
            @Param("nameFilter") String nameFilter
    );
}