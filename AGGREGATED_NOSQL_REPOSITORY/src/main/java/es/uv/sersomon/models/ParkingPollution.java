package es.uv.sersomon.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPollution {

    private Integer id;
    private Double averageBikesAvailable;
    private AirQuality airQuality;
}
