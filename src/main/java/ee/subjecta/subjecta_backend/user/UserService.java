package ee.subjecta.subjecta_backend.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository,
                       PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User register(String username, String email, String rawPassword) {

        if (repository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String hashed = encoder.encode(rawPassword);

        User user = new User(username, email, hashed);

        return repository.save(user);
    }

    public User authenticate(String username, String rawPassword) {

        User user = repository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified");
        }

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}