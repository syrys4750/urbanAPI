package es.uv.sersomon.models;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
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
