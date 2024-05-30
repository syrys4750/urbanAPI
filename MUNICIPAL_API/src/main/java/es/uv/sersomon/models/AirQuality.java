package es.uv.sersomon.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQuality {
    private Double nitricOxides;

    private Double nitrogenDioxides;

    private Double vocs_nmhc;

    private Double pm2_5;
}
