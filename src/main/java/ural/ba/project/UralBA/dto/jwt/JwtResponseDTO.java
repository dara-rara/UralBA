package ural.ba.project.UralBA.dto.jwt;

/**
 * @author Daria
 */
public class JwtResponseDTO {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

    public JwtResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
