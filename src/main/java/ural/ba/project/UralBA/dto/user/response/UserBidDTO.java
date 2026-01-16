package ural.ba.project.UralBA.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Отображение заявки пользователя
 *
 * @author Daria
 */
public record UserBidDTO(
        long id,
        String name,
        String position,
        String company,
        String email,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String reason
) { }
