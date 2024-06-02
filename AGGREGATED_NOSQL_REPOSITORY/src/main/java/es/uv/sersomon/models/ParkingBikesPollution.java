package es.uv.sersomon.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBikesPollution {
    @NotNull
    private Integer idParking; // this way, _id is not overriden
    @NotNull
    private Double averageBikesAvailable;
    @NotNull
    private AirQuality airQuality;
}
