package es.uv.sersomon.exceptions;

public class NotFoundStationException extends RuntimeException {
    public NotFoundStationException(String message) {
        super(message);
    }

    public NotFoundStationException(String message, Throwable cause) {
        super(message, cause);
    }
}
