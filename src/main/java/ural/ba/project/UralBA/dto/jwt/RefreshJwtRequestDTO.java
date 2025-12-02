package ural.ba.project.UralBA.dto.jwt;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Daria
 */
public record RefreshJwtRequestDTO(
        @NotBlank String refreshToken
) {
}
