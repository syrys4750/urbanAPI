package es.uv.sersomon.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aggregate {
    private LocalDateTime timeStamp;
    private List<ParkingBikesPollution> aggregatedData;
}
