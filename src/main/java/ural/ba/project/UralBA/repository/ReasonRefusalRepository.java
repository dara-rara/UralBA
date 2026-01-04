package ural.ba.project.UralBA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ural.ba.project.UralBA.model.ReasonRefusal;
import ural.ba.project.UralBA.model.User;

import java.util.Optional;

/**
 * @author Daria
 */
public interface ReasonRefusalRepository extends JpaRepository<ReasonRefusal, Long> {

    Optional<ReasonRefusal> findByUser(User user);
}
