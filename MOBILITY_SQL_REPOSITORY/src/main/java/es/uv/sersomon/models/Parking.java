package es.uv.sersomon.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking")
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String direction;
    @Column
    private Integer bikesCapacity;

    @Column
    private Double latitude;
    @Column
    private Double longitude;

    public Parking(int id, String direction, int bikesCapacity, double latitude, double longitude) {
        this.id = Integer.valueOf(id);
        this.direction = direction;
        this.bikesCapacity = Integer.valueOf(bikesCapacity);
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }

}
