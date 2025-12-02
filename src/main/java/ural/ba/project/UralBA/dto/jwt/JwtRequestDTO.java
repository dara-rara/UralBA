package ural.ba.project.UralBA.dto.jwt;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Daria
 */
public record JwtRequestDTO(
        @NotBlank String email,
        @NotBlank String password
) {
}