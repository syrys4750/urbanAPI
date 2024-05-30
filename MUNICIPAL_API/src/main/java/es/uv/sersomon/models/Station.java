package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    private Integer id;
    private String direction;
    private Double longitude;
    private Double latitude;
}
