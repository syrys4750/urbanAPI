package es.uv.sersomon.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uv.sersomon.exceptions.AlreadyExistsStationException;
import es.uv.sersomon.exceptions.InvalidStationException;
import es.uv.sersomon.exceptions.NotFoundStationException;
import es.uv.sersomon.models.Station;
import es.uv.sersomon.services.StationService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Controller
@RequestMapping("/api/v1")
public class StationController {
    @Autowired
    StationService stationService;

    @GetMapping("/estaciones")
    @Operation(summary = "Find All Stations", description = "Retrieves all stations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stations retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class)) })
    })
    public ResponseEntity<List<Station>> findStations() {
        return new ResponseEntity<>(stationService.findAllStations(), HttpStatus.OK);
    }

    @GetMapping("/estaciones/{id}")
    @Operation(summary = "Find Station by ID", description = "Retrieves a station by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class)) }),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content)
    })
    public ResponseEntity<Station> findStationById(@PathVariable int id) {
        Optional<Station> station = stationService.findStationById(id);
        if (station.isEmpty()) {
            return new ResponseEntity<>(new Station(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(station.get(), HttpStatus.OK);
    }

    @PostMapping("/estacion")
    @Operation(summary = "Create Station", description = "Creates a new station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
    })
    public ResponseEntity<Station> createStation(@RequestBody @Valid Station station) {
        if (stationService.stationExistsByLocation(station)) {
            new ResponseEntity<>(new Station(), HttpStatus.CONFLICT);
        }
        station.setId(null);
        Station createdStation = stationService.createStation(station);
        return new ResponseEntity<>(createdStation, HttpStatus.OK);
    }

    @PutMapping("/estacion/{id}")
    @Operation(summary = "Update Station", description = "Updates a station by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Station.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
    })
    public ResponseEntity<Station> updateStation(@PathVariable int id, @RequestBody @Valid Station station) {
        if (station.getId() == null)
            station.setId(id);
        if (station.getId().intValue() != id) {
            station.setId(id);
        }
        if (!stationService.existsStation(station.getId()))
            throw new NotFoundStationException("Station not found");

        Station updatedStation = stationService.updateStation(station);
        return new ResponseEntity<>(updatedStation, HttpStatus.OK);
    }

    @DeleteMapping("/estacion/{id}")
    @Operation(summary = "Delete Station", description = "Deletes a station by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
    })
    public ResponseEntity<String> deleteStation(@PathVariable Integer id) {
        if (!stationService.existsStation(id))
            throw new NotFoundStationException("Station not found");

        stationService.deleteStationById(id);
        return new ResponseEntity<>("Station with ID " + id + " deleted", HttpStatus.OK);
    }

}
