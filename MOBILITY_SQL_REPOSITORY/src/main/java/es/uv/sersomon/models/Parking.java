package es.uv.sersomon.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking")
@SequenceGenerator(name = "parking_seq", sequenceName = "parking_sequence", initialValue = 11, allocationSize = 1)
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_seq")
    private Integer id;

    @Column
    @NotNull
    private String direction;
    @Column
    @NotNull
    private Integer bikesCapacity;

    @Column
    @NotNull
    private Double latitude;
    @Column
    @NotNull
    private Double longitude;

    public Parking(int id, String direction, int bikesCapacity, double latitude, double longitude) {
        this.id = Integer.valueOf(id);
        this.direction = direction;
        this.bikesCapacity = Integer.valueOf(bikesCapacity);
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }

}
