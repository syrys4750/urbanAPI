package es.uv.sersomon.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Measurement {
    private Integer idStation;
    private LocalDateTime timestamp;
    private Double nitricOxides;
    private Double nitrogenDioxides;
    private Double vocs_nmhc;
    private Double pm2_5;
}
