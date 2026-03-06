package ee.subjecta.subjecta_backend.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}