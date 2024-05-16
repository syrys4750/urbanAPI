package es.uv.sersomon.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Event;
import es.uv.sersomon.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List<Event> findEventByIdParking(int id) {
        return eventRepository.findByIdParking(id);
    }

    public boolean existsByIdParking(int id) {
        return eventRepository.existsByIdParking(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findEventByIdParkingBetweenFromToByTimestampDesc(int id, LocalDateTime from, LocalDateTime to) {
        return eventRepository.findByIdParkingAndTimestampBetweenOrderByTimestampDesc(id, from, to);
    }

    public Event findEventByIdParkingRecent(int id) {
        return eventRepository.findFirstByIdParkingOrderByTimestampDesc(id);
    }

}
