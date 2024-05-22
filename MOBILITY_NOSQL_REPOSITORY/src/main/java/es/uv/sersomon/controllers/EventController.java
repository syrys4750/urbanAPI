package es.uv.sersomon.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.sersomon.models.Event;
import es.uv.sersomon.services.EventService;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.uv.sersomon.DTOs.StatusDTO;
import es.uv.sersomon.enums.Operations;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    @Autowired
    EventService eventService;

    private static final List<String> PERMITTED_OPERATIONS = Arrays.stream(Operations.values())
            .map(Operations::toString).collect(Collectors.toList());

    @GetMapping("/evento/{id}/status")
    public ResponseEntity<?> findEventById(@PathVariable("id") int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        // List<Event> events = eventService.findEventByIdParking(id);

        if (!eventService.existsByIdParking(id)) {
            return new ResponseEntity<>("Parking with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (from == null || to == null) {
            Event mostRecentEvent = eventService.findEventByIdParkingRecent(id);
            return new ResponseEntity<StatusDTO>(new StatusDTO(mostRecentEvent.getIdParking(),
                    mostRecentEvent.getBikesAvailable(), mostRecentEvent.getFreeParkingSpots(),
                    mostRecentEvent.getTimestamp()), HttpStatus.OK);
        } else {
            List<Event> eventsFromTo = eventService.findEventByIdParkingBetweenFromToByTimestampDesc(id, from, to);
            return new ResponseEntity<List<StatusDTO>>(eventsFromTo.stream()
                    .map(e -> new StatusDTO(Integer.valueOf(e.getIdParking()), e.getBikesAvailable(),
                            e.getFreeParkingSpots(), e.getTimestamp()))
                    .collect(Collectors.toList()),
                    HttpStatus.OK);
        }

    }

    @PostMapping("/evento")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        // if (eventService.existsById(event.getId()))
        // return new ResponseEntity<String>("Event already exists",
        // HttpStatus.CONFLICT);
        if (!PERMITTED_OPERATIONS.contains(event.getOperation())) {
            return new ResponseEntity<String>("Operation type " + event.getOperation() + " is not permitted",
                    HttpStatus.BAD_REQUEST);
        }
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<Event>(createdEvent, HttpStatus.CREATED);
    }

}
