package es.uv.sersomon.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class InvalidStationException extends RuntimeException {
    public InvalidStationException(String message) {
        super(message);
    }

    public InvalidStationException(String message, Throwable cause) {
        super(message, cause);
    }
}
