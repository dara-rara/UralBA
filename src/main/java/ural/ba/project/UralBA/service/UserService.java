package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.UserRepository;

/**
 * Предоставляет методы для создания, проверки существования и поиска пользователей
 *
 * @author Daria
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создает нового пользователя в системе
     *
     * @throws IllegalArgumentException Если пользователь с таким email уже существует.
     */
    public User create(User user) {
        String email = user.getEmail();

        if (emailExists(email)) {
            throw new IllegalArgumentException(
                    "Пользователь с email = " + email + " уже существует"
            );
        }

        return userRepository.save(user);
    }

    /**
     * Проверяет наличие пользователя с указанным email
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Ищет пользователя по email
     *
     * @throws ResourceNotFoundException Если пользователь с указанным email не найден
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email=" + email));
    }
}
