package ural.ba.project.UralBA.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.validation.ValidEnum;

/**
 * DTO-запрос обработка заявки пользователя
 *
 * @author Daria
 */
public record BidRequestDTO(
        @NotNull long idUser,
        @NotBlank @ValidEnum(enumClass = Role.class) String role,
        String reason
) {
}