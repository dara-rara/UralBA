package ural.ba.project.UralBA.dto.user.response;

/**
 * DTO-ответ c короткой ин-цией о пользователе
 *
 * @author Daria
 */
public record UserShortResponseDTO(
        long id,
        String name,
        String email
) { }
