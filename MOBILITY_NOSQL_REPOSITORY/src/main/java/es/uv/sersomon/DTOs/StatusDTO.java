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
}
