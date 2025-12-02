package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.UserRepository;

/**
 * @author Daria
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        String email = user.getEmail();

        if (emailExists(email)) {
            throw new IllegalArgumentException(
                    "Пользователь с email = " + email + " уже существует"
            );
        }

        return userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email=" + email));
    }
}
