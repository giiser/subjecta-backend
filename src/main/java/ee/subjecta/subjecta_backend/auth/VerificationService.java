package ee.subjecta.subjecta_backend.auth;

import ee.subjecta.subjecta_backend.user.User;
import ee.subjecta.subjecta_backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public VerificationService(
            VerificationTokenRepository tokenRepository,
            UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public String createToken(String username) {

        String token = UUID.randomUUID().toString();

        VerificationToken verification = new VerificationToken();
        verification.setToken(token);
        verification.setUsername(username);
        verification.setExpiresAt(
                Instant.now().plus(Duration.ofHours(24))
        );

        tokenRepository.save(verification);

        return token;
    }

    public void verify(String token) {

        VerificationToken stored = tokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid verification token"));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Verification token expired");
        }

        User user = userRepository.findByUsername(stored.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(stored);
    }
}
