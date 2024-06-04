package es.uv.sersomon.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.sersomon.models.Event;
import es.uv.sersomon.services.EventService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.uv.sersomon.DTOs.StatusDTO;
import es.uv.sersomon.enums.Operations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1")
public class EventController {
        @Autowired
        EventService eventService;

        private static final List<String> PERMITTED_OPERATIONS = Arrays.stream(Operations.values())
                        .map(Operations::toString).collect(Collectors.toList());

        @GetMapping("/aparcamiento/{id}/status")
        @Operation(summary = "Find Event by ID", description = "Retrieves event status by parking ID and optionally within a date range")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Status retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = StatusDTO[].class)) }),
                        @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content)
        })
        public ResponseEntity<?> findEventById(@PathVariable("id") int id,
                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

                if (from == null || to == null) {
                        Event mostRecentEvent = eventService.findEventByIdParkingRecent(id);
                        if (mostRecentEvent == null) {
                                return new ResponseEntity<StatusDTO>(new StatusDTO(), HttpStatus.NOT_FOUND);
                        }
                        return new ResponseEntity<StatusDTO>(new StatusDTO(mostRecentEvent.getIdParking(),
                                        mostRecentEvent.getBikesAvailable(), mostRecentEvent.getFreeParkingSpots(),
                                        mostRecentEvent.getTimestamp()), HttpStatus.OK);
                } else {
                        List<Event> eventsFromTo = eventService.findEventByIdParkingBetweenFromToByTimestampDesc(id,
                                        from, to);
                        return new ResponseEntity<List<StatusDTO>>(eventsFromTo.stream()
                                        .map(e -> new StatusDTO(Integer.valueOf(e.getIdParking()),
                                                        e.getBikesAvailable(),
                                                        e.getFreeParkingSpots(), e.getTimestamp()))
                                        .collect(Collectors.toList()),
                                        HttpStatus.OK);
                }
        }

        @PostMapping("/evento/{id}")
        @Operation(summary = "Create Event", description = "Creates a new event associated with a parking ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Event created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_PARKING)", content = @Content)
        })
        public ResponseEntity<?> createEvent(@RequestBody @Valid Event event, @PathVariable int id) {
                if (!PERMITTED_OPERATIONS.contains(event.getOperation())) {
                        return new ResponseEntity<String>(
                                        "Operation type " + event.getOperation() + " is not permitted",
                                        HttpStatus.BAD_REQUEST);
                }
                if (event.getIdParking().intValue() != id) {
                        return new ResponseEntity<String>(
                                        "Specified idParking is different from /event/{id} on URL ",
                                        HttpStatus.BAD_REQUEST);
                }
                event.setIdParking(Integer.valueOf(id));

                event.setId(null);
                Event createdEvent = eventService.createEvent(event);
                return new ResponseEntity<Event>(createdEvent, HttpStatus.CREATED);
        }

        @GetMapping("/top10")
        @Operation(summary = "Get Top 10 Parkings by Availability", description = "Retrieves the top 10 parkings by current availability")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Top 10 parkings retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Integer[].class)) })
        })
        public ResponseEntity<List<Integer>> findTop10ParkingsByDisponibility() {
                return new ResponseEntity<>(
                                eventService.findTop10ParkingsByDisponibility(), HttpStatus.OK);
        }

}
