package es.uv.sersomon.models;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Measurement {
    @NotNull
    private Integer idStation;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private Double nitricOxides;
    @NotNull
    private Double nitrogenDioxides;
    @NotNull
    private Double vocs_nmhc;
    @NotNull
    private Double pm2_5;
}
