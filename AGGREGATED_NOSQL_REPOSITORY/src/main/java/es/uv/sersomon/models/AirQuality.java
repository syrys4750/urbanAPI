package es.uv.sersomon.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQuality {
    @NotNull
    private Double nitricOxides;
    @NotNull
    private Double nitrogenDioxides;
    @NotNull
    private Double vocs_nmhc;
    @NotNull
    private Double pm2_5;
}
