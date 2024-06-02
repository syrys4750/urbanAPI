package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParkingToken {
    private Parking parking;
    private String jwtToken;
}
