package es.uv.sersomon.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.uv.sersomon.models.Event;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findById(int id);

    List<Event> findByIdParkingAndTimestampBetweenOrderByTimestampDesc(int idParking, LocalDateTime from,
            LocalDateTime to);

    List<Event> findByIdParking(int id);

    boolean existsByIdParking(int id);

    Event findFirstByIdParkingOrderByTimestampDesc(int idParking);
}
