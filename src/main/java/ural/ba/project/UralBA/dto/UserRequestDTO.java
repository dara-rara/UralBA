package ural.ba.project.UralBA.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Daria
 */
public record UserRequestDTO(
        @NotBlank String name,
        @Email(regexp =
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
        )
        @NotBlank
        String email,
        @NotBlank String password,
        String position,
        String company
) {}