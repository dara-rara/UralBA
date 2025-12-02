package ural.ba.project.UralBA.service;

import org.springframework.stereotype.Service;
import ural.ba.project.UralBA.exepction.ResourceNotFoundException;
import ural.ba.project.UralBA.model.RefreshToken;
import ural.ba.project.UralBA.model.User;
import ural.ba.project.UralBA.repository.RefreshTokenRepository;

/**
 * @author Daria
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken findByUser(User user) {
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Токен", "id_user=" + user.getId_user()));
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}
