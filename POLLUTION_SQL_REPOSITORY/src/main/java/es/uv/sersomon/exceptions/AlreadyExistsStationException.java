package es.uv.sersomon.exceptions;

public class AlreadyExistsStationException extends RuntimeException {
    public AlreadyExistsStationException(String message) {
        super(message);
    }

    public AlreadyExistsStationException(String message, Throwable cause) {
        super(message, cause);
    }
}
