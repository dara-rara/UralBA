package ural.ba.project.UralBA.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ural.ba.project.UralBA.dto.user.response.UserBidDTO;
import ural.ba.project.UralBA.dto.user.response.UserResponseDTO;
import ural.ba.project.UralBA.dto.user.response.UserShortResponseDTO;
import ural.ba.project.UralBA.model.ReasonRefusal;
import ural.ba.project.UralBA.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Маппер для сущности User
 *
 * @author Daria
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "user.idUser")
    UserBidDTO toUserBidResponseDTO(User user, String reason);

    @Mapping(target = "id", source = "idUser")
    default UserBidDTO toUserBidResponseDTO(User user) {
        return toUserBidResponseDTO(user, null);
    }


    default List<UserBidDTO> toUserBidResponseDTOList(List<User> users) {
        return users.stream()
                .map(this::toUserBidResponseDTO)
                .collect(Collectors.toList());
    }

    default List<UserBidDTO> toUserBidResponseDTOList(
            List<User> users,
            List<ReasonRefusal> reasonRefusals) {

        Map<Long, String> reasonMap = reasonRefusals.stream()
                .collect(Collectors.toMap(
                        reason -> reason.getUser().getIdUser(),
                        ReasonRefusal::getReason
                ));

        return users.stream()
                .map(user -> {
                    String reason = reasonMap.get(user.getIdUser());
                    return toUserBidResponseDTO(user, reason);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", source = "idUser")
    UserShortResponseDTO toUserShortResponseDTO(User user);

    List<UserShortResponseDTO> toUserShortResponseDTOList(List<User> users);

    @Mapping(target = "id", source = "idUser")
    UserResponseDTO toUserResponseDTO(User user);
}