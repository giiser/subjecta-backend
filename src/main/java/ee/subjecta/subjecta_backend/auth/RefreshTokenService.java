package ee.subjecta.subjecta_backend.auth;

import ee.subjecta.subjecta_backend.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository repository,
                               JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public void save(String token, String username) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpiresAt(
                Instant.now().plusMillis(jwtService.getRefreshExpiration())
        );
        refreshToken.setRevoked(false);

        repository.save(refreshToken);
    }

    public void validate(String token) {

        RefreshToken stored = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        if (stored.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }
    }

    public void revoke(String token) {

        RefreshToken stored = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        stored.setRevoked(true);
        repository.save(stored);
    }
}