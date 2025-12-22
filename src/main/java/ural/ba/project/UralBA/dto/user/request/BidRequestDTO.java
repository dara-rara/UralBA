package ural.ba.project.UralBA.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.validation.ValidEnum;

/**
 * DTO-запрос обработка заявки пользователя
 * @author Daria
 */
public record BidRequestDTO(long idUser, @NotBlank @ValidEnum(enumClass = Role.class) String role) {
}
