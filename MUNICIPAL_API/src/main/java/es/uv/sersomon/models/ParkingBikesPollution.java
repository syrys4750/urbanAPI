package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBikesPollution {

    private Integer idParking;
    private Double averageBikesAvailable;
    private AirQuality airQuality;
}
