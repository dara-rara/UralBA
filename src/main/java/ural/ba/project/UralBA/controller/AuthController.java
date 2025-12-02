package ural.ba.project.UralBA.controller;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ural.ba.project.UralBA.dto.jwt.JwtRequestDTO;
import ural.ba.project.UralBA.dto.jwt.JwtResponseDTO;
import ural.ba.project.UralBA.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO jwtRequestDTO
    ) throws AuthException {
        JwtResponseDTO response = authService.login(jwtRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDTO> getNewAccessToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        JwtResponseDTO response = authService.getAccessToken(authHeader);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> getNewRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws AuthException {
        JwtResponseDTO response = authService.refresh(authHeader);
        return ResponseEntity.ok(response);
    }
}