package ee.subjecta.subjecta_backend.auth;

import ee.subjecta.subjecta_backend.user.User;
import ee.subjecta.subjecta_backend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            PasswordEncoder encoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public String createToken(String email) {

        User user = userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsername(user.getUsername());
        resetToken.setExpiresAt(
                Instant.now().plus(Duration.ofHours(1))
        );

        tokenRepository.save(resetToken);

        return token;
    }

    public void resetPassword(String token, String newPassword) {

        PasswordResetToken stored = tokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token"));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Reset token expired");
        }

        User user = userRepository.findByUsername(stored.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(stored);
    }
}