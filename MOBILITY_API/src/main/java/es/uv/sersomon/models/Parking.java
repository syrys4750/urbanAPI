package es.uv.sersomon.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Parking {

    @NotBlank
    private String direction;
    @NotNull
    private Integer bikesCapacity;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

}