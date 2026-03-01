package ee.subjecta.subjecta_backend.api.error;

import java.time.Instant;

public record ApiError(
        int status,
        String error,
        String message,
        Instant timestamp
) {
}
