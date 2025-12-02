package ural.ba.project.UralBA.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Daria
 */
public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String position
) {}