package ee.subjecta.subjecta_backend.exception;

public class CmsException extends RuntimeException {
    public CmsException(String message, Throwable cause) {
        super(message, cause);
    }
}