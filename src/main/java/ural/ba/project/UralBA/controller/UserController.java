package ural.ba.project.UralBA.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ba.project.UralBA.dto.UserRequestDTO;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.service.RefreshTokenService;
import ural.ba.project.UralBA.service.UserService;

/**
 * Контроллер для управления пользователями и связанными с ними данными
 *
 * @author Daria
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Эндпоинт для создания нового пользователя в системе
     * Хэширует пароль, устанавливает роль по умолчанию и генерирует пустую сущность Refresh Token.
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setPosition(userRequestDTO.position());
        user.setRole(Role.NEW_BID);
        userService.create(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenService.save(refreshToken);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasAuthority('USER')")
//    @GetMapping("/get")
//    public ResponseEntity<?> get() {
//        return ResponseEntity.ok().body("test");
//    }
}
