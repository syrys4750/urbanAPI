package es.uv.sersomon.models;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Event {
    @NotNull
    private int idParking;
    @NotBlank
    private String operation;
    @NotNull
    private Integer bikesAvailable;
    @NotNull
    private Integer freeParkingSpots;
    @NotNull
    private LocalDateTime timestamp;
}
