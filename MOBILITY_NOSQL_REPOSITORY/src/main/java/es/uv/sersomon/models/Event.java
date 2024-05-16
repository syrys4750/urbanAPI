package es.uv.sersomon.models;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("events")
@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO: me hace falta un @id? si me da igual que esten repetidos
public class Event {
    @Id
    private String id;
    private int idParking;
    private String operation;
    private Integer bikesAvailable;
    private Integer freeParkingSpots;
    private LocalDateTime timestamp;
}
