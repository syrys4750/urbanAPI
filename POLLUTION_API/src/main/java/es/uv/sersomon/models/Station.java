package es.uv.sersomon.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Station {
    @NotBlank
    private String direction;
    @NotNull
    private Float altitude;
    @NotNull
    private Float latitude;
}
