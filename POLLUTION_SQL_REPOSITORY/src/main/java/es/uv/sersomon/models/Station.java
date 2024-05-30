package es.uv.sersomon.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stations")
@SequenceGenerator(name = "station_seq", sequenceName = "station_sequence", initialValue = 11, allocationSize = 1)
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_seq")
    private Integer id;
    @NotBlank
    private String direction;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
}
