package ural.ba.project.UralBA.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ural.ba.project.UralBA.dto.user.request.BidRequestDTO;
import ural.ba.project.UralBA.dto.user.request.UserRequestDTO;
import ural.ba.project.UralBA.dto.user.response.*;
import ural.ba.project.UralBA.mapper.UserMapper;
import ural.ba.project.UralBA.model.ReasonRefusal;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.service.ReasonRefusalService;
import ural.ba.project.UralBA.service.RefreshTokenService;
import ural.ba.project.UralBA.service.UserService;

import java.util.List;

/**
 * Контроллер для управления пользователями и связанными с ними данными
 *
 * @author Daria
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ReasonRefusalService reasonRefusalService;

    public UserController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder,
                          RefreshTokenService refreshTokenService, ReasonRefusalService reasonRefusalService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.reasonRefusalService = reasonRefusalService;
    }

    /**
     * Эндпоинт для создания нового пользователя в системе
     * Хэширует пароль, устанавливает роль по умолчанию и генерирует пустую сущность Refresh Token
     */
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = new User(
                userRequestDTO.name(),
                userRequestDTO.email(),
                passwordEncoder.encode(userRequestDTO.password()),
                userRequestDTO.position(),
                userRequestDTO.company(),
                Role.NEW_BID
        );
        userService.create(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenService.save(refreshToken);

        return ResponseEntity.ok().build();
    }

    /**
     * Эндпоинт для показа роли пользователя (права доступа)
     */
    @Transactional(readOnly = true)
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@AuthenticationPrincipal String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(new RoleResponseDTO(user.getRole().getAuthority()));
    }

    /**
     * Эндпоинт для смены роли
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/updateRole")
    public ResponseEntity<?> editRole(@Valid @RequestBody BidRequestDTO bidRequestDTO) {
        User user = userService.findById(bidRequestDTO.idUser());
        Role role = Role.valueOf(bidRequestDTO.role());

        ReasonRefusal reasonRefusal = new ReasonRefusal(
                bidRequestDTO.reason(),
                user
        );
        reasonRefusalService.saveAndDeleteCheck(reasonRefusal, role, user);

        user.setRole(role);
        userService.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * Эндпоинт для показа заявок
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/bids")
    public ResponseEntity<?> getBidList() {
        List<User> usersNewBid = userService.findByRoleAndFilter(Role.NEW_BID, null);
        List<UserBidDTO> usersNewBidDTO = userMapper.toUserBidResponseDTOList(usersNewBid);

        List<User> usersRejectedBid = userService.findByRoleAndFilter(Role.REJECTED_BID, null);
        List<ReasonRefusal> reasonRefusals = reasonRefusalService.findByAll();
        List<UserBidDTO> usersRejectedBidDTO = userMapper.toUserBidResponseDTOList(usersRejectedBid, reasonRefusals);

        return ResponseEntity.ok(new UserBidsResponseDTO(usersNewBidDTO, usersRejectedBidDTO));
    }

    /**
     * Эндпоинт для показа всех пользователей с ролью USER с фильтрацией и их кол-во
     * @param nameFilter - опциональный параметр фильтрации по имени
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "") String nameFilter) {

        String filter = nameFilter.isEmpty() ? null : nameFilter;
        List<User> users = userService.findByRoleAndFilter(Role.USER, filter);
        List<UserShortResponseDTO> usersDTO = userMapper.toUserShortResponseDTOList(users);
        UserAllResponseDTO userResponseDTO = new UserAllResponseDTO(
                usersDTO.size(),
                usersDTO
        );

        return ResponseEntity.ok(userResponseDTO);
    }

    /**
     * Эндпоинт для получения одного пользователя
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        User user = userService.findById(id);
        UserResponseDTO userDTO = userMapper.toUserResponseDTO(user);

        return ResponseEntity.ok(userDTO);
    }

//    @PreAuthorize("hasAuthority('USER')")
//    @GetMapping("/get")
//    public ResponseEntity<?> get() {
//        return ResponseEntity.ok().body("test");
//    }
}
