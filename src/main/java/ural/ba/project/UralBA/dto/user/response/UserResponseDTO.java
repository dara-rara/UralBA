package ural.ba.project.UralBA.dto.user.response;

/**
 * DTO-ответ со всей информацией о пользователе
 *
 * @author Daria
 */
public record UserResponseDTO(
        long id,
        String name,
        String position,
        String company,
        String email,
        String description
) {
}
