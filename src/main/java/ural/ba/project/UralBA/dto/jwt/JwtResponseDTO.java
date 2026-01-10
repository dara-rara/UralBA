package ural.ba.project.UralBA.dto.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Daria
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseDTO {
    private String accessToken;
    private String refreshToken;

    public JwtResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
