package ural.ba.project.UralBA.dto.user.response;

import java.util.List;

/**
 * DTO-ответ для отображения списка пользователей и их кол-во
 *
 * @author Daria
 */
public record UserAllResponseDTO(
        int count,
        List<UserShortResponseDTO> users
) {
}
