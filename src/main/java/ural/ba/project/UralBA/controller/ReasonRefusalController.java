package ural.ba.project.UralBA.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ba.project.UralBA.dto.ReasonRefusalRequestDTO;
import ural.ba.project.UralBA.model.ReasonRefusal;
import ural.ba.project.UralBA.model.Role;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.service.ReasonRefusalService;
import ural.ba.project.UralBA.service.UserService;

/**
 * Контроллер для управления причин отказа принять пользователя
 *
 * @author Daria
 */
@RestController
@RequestMapping("/reason")
public class ReasonRefusalController {

    private final ReasonRefusalService reasonRefusalService;
    private final UserService userService;

    public ReasonRefusalController(ReasonRefusalService reasonRefusalService, UserService userService) {
        this.reasonRefusalService = reasonRefusalService;
        this.userService = userService;
    }

    /**
     * Эндпоинт для создания причины отказа в системе
     * Устанавливает пользователя новую роль REJECTED_BID
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ReasonRefusalRequestDTO reasonRefusalRequestDTO) {
        User user = userService.findById(reasonRefusalRequestDTO.idUser());
        user.setRole(Role.REJECTED_BID);
        userService.save(user);

        ReasonRefusal reasonRefusal = new ReasonRefusal(
                reasonRefusalRequestDTO.reason(),
                user
        );
        reasonRefusalService.save(reasonRefusal);

        return ResponseEntity.ok().build();
    }
}
