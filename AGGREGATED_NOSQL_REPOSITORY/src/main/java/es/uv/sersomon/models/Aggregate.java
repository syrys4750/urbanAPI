package es.uv.sersomon.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("aggregates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate {
    @NotNull
    private LocalDateTime timeStamp;
    @NotNull
    private List<ParkingBikesPollution> aggregatedData;
}
