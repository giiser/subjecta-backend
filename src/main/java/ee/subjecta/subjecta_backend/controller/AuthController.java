package ee.subjecta.subjecta_backend.controller;

import ee.subjecta.subjecta_backend.auth.PasswordResetService;
import ee.subjecta.subjecta_backend.auth.RefreshTokenService;
import ee.subjecta.subjecta_backend.auth.VerificationService;
import ee.subjecta.subjecta_backend.client.contentful.ContentfulHttpClient;
import ee.subjecta.subjecta_backend.email.EmailService;
import ee.subjecta.subjecta_backend.security.JwtService;
import ee.subjecta.subjecta_backend.user.User;
import ee.subjecta.subjecta_backend.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final VerificationService verificationService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;


    public AuthController(JwtService jwtService,
                          UserService userService,
                          RefreshTokenService refreshTokenService,
                          VerificationService verificationService,
                          EmailService emailService,
                          PasswordResetService passwordResetService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.verificationService = verificationService;
        this.emailService = emailService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public Map<String, String> register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        userService.register(username, email, password);

        String token = verificationService.createToken(username);

        log.info("Verification link: http://localhost:8080/api/v1/auth/verify?token={}", token);
        String verificationMailContent = String.format("Verification link: http://localhost:8080/api/v1/auth/verify?token={%s}", token);
        emailService.send(email, "Verification link", verificationMailContent);

        return Map.of("status", "registered - check email for verification link");
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestParam String username,
            @RequestParam String password) {

        User user = userService.authenticate(username, password);

        String accessToken =
                jwtService.generateAccessToken(user.getUsername());

        String refreshToken =
                jwtService.generateRefreshToken(user.getUsername());

        refreshTokenService.save(refreshToken, user.getUsername());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(
            @RequestParam String refreshToken) {

        refreshTokenService.validate(refreshToken);

        String username = jwtService.extractUsername(refreshToken);

        String newAccess = jwtService.generateAccessToken(username);

        return Map.of("accessToken", newAccess);
    }

    @GetMapping("/verify")
    public Map<String, String> verify(
            @RequestParam String token) {

        verificationService.verify(token);

        return Map.of("status", "account_verified");
    }

    @PostMapping("/logout")
    public Map<String, String> logout(
            @RequestParam String refreshToken) {

        refreshTokenService.revoke(refreshToken);

        return Map.of("status", "logged_out");
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(
            @RequestParam String email) {

        String token = passwordResetService.createToken(email);

        String link = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;

        emailService.send(
                email,
                "Password Reset",
                "Click the link to reset your password:\n" + link
        );

        return Map.of("status", "reset_email_sent");
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        passwordResetService.resetPassword(token, newPassword);

        return Map.of("status", "password_reset_successful");
    }
}