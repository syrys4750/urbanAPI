package es.uv.sersomon.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    private Integer id;
    @NotBlank
    private String direction;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
}
