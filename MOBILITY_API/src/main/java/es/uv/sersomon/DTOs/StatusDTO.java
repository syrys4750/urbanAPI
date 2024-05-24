package es.uv.sersomon.DTOs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    private Integer idParking;
    private Integer bikesAvailable;

    private Integer freeParkingSpots;
    private LocalDateTime timestamp;

    public StatusDTO(int idParking, Integer bikesAvailable, Integer freeParkingSpots, LocalDateTime timestamp) {
        this.idParking = Integer.valueOf(idParking);
        this.bikesAvailable = bikesAvailable;
        this.freeParkingSpots = freeParkingSpots;
        this.timestamp = timestamp;
    }

}
