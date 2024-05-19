package es.uv.sersomon.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.uv.sersomon.models.Measurement;

public interface MeasurementRepository extends MongoRepository<Measurement, Integer> {

    Measurement findFirstByIdStationOrderByTimestampDesc(int idStation);

    List<Measurement> findByIdStationAndTimestampBetweenOrderByTimestampDesc(Integer idStation, LocalDateTime from,
            LocalDateTime to);

}
