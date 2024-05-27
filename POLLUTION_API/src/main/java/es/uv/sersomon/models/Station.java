package es.uv.sersomon.models;

import java.math.BigDecimal;

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
    private BigDecimal altitude;
    @NotNull
    private BigDecimal latitude;
}
