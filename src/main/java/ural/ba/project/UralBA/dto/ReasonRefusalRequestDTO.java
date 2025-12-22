package ural.ba.project.UralBA.dto;

import jakarta.validation.constraints.NotNull;

/**
 * @author Daria
 */
public record ReasonRefusalRequestDTO(
        @NotNull long idUser,
        String reason
) {
}
