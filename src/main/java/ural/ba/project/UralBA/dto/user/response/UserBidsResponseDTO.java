package ural.ba.project.UralBA.dto.user.response;

import java.util.List;

/**
 * DTO-ответ новых и отклоненных заявок пользователей
 *
 * @author Daria
 */
public record UserBidsResponseDTO(
        List<UserBidDTO> newBid,
        List<UserBidDTO> rejectedBid
) {
}
