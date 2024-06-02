package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StationToken {
    private Station station;
    private String jwtToken;
}
