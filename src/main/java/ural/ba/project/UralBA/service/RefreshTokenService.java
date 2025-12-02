package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.RefreshTokenRepository;

/**
 * Предоставляет методы для поиска и сохранения Refresh токенов, связанных с пользователями
 *
 * @author Daria
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Ищет Refresh токен по указанному пользователю
     *
     * @throws ResourceNotFoundException Если токен для данного пользователя не найден в базе данных.
     */
    public RefreshToken findByUser(User user) {
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Токен", "id_user=" + user.getId_user()));
    }

    /**
     * Сохраняет или обновляет предоставленный Refresh токен
     */
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}

