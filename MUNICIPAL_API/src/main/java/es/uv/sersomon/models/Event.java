package es.uv.sersomon.models;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO: me hace falta un @id? si me da igual que esten repetidos
public class Event {
    private int idParking;
    private Integer bikesAvailable;
    private Integer freeParkingSpots;
    private LocalDateTime timestamp;
}
