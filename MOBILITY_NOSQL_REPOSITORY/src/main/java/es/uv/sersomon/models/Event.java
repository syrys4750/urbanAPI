package es.uv.sersomon.models;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    private String id;
    @NotNull
    private Integer idParking;
    @NotBlank
    private String operation;
    @NotNull
    private Integer bikesAvailable;
    @NotNull
    private Integer freeParkingSpots;
    @NotNull
    private LocalDateTime timestamp;
}
