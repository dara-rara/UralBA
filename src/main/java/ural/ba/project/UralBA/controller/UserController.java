package ural.ba.project.UralBA.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ural.ba.project.UralBA.dto.UserRequestDTO;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.service.RefreshTokenService;
import ural.ba.project.UralBA.service.UserService;

/**
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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok().body("test");
    }
}
