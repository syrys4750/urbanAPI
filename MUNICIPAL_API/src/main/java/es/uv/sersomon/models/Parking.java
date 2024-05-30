package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parking {
    private Integer id;
    private String direction;
    private Integer bikesCapacity;
    private Double latitude;
    private Double longitude;

}