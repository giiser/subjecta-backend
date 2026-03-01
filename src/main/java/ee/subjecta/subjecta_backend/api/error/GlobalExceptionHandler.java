package ee.subjecta.subjecta_backend.api.error;

import ee.subjecta.subjecta_backend.exception.CmsException;
import ee.subjecta.subjecta_backend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFound(NotFoundException ex) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(CmsException.class)
    public ApiError handleCms(CmsException ex) {
        return new ApiError(
                HttpStatus.BAD_GATEWAY.value(),
                "CMS_ERROR",
                "Content service unavailable",
                Instant.now()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiError handleGeneric(Exception ex) {
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Unexpected server error",
                Instant.now()
        );
    }
}