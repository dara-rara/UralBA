package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.UserRepository;

import java.util.List;

/**
 * Предоставляет методы для создания, редактирования и поиска пользователей
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
    public void create(User user) {
        String email = user.getEmail();

        if (emailExists(email)) {
            throw new IllegalArgumentException(
                    "Пользователь с email = " + email + " уже существует"
            );
        }
        save(user);
    }

    /**
     * Сохраняет пользователя в системе
     */
    public User save(User user) {
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

    /**
     * Ищет пользователя по id
     *
     * @throws ResourceNotFoundException Если пользователь с указанным id не найден
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id=" + id));
    }

    /**
     * Отдаёт список пользователей по роли с фильтрацией по имени
     */
    public List<User> findByRoleAndFilter(Role role, String nameFilter) {
        return userRepository.findByRoleAndFilter(role, nameFilter);
    }
}
