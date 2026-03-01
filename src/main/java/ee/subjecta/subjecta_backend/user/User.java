package ee.subjecta.subjecta_backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Getter
    @Setter
    @Column(nullable = false, length = 100)
    private String password;

    @Getter
    @Setter
    private String role = "USER";

    @Getter
    @Setter
    private Instant createdAt = Instant.now();

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean enabled = false;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
