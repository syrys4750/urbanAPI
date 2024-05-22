package es.uv.sersomon.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// TODO: los campos en mayuscula no me funcionan
@Document("measurements")
@Data
public class Measurement {
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
