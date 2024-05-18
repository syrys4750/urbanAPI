package es.uv.sersomon.controllers;

import java.util.List;
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

@Controller
@RequestMapping("/api/v1")
public class StationController {
    @Autowired
    StationService stationService;

    @GetMapping("/estaciones")
    public ResponseEntity<List<Station>> findStations() {
        return new ResponseEntity<>(stationService.findAllStations(), HttpStatus.OK);
    }

    @PostMapping("/estacion")
    public ResponseEntity<Station> createStation(@RequestBody @Valid Station station) {

        if (stationService.existsByDirectionAltitudeLatitude(station.getDirection(), station.getAltitude().floatValue(),
                station.getLatitude().floatValue())) {
            throw new AlreadyExistsStationException("Station already exists on direction " + station.getDirection()
                    + ", altitude " + station.getAltitude() + ", latitude " + station.getLatitude());
        }

        Station createdStation = stationService.createStation(station);
        return new ResponseEntity<>(createdStation, HttpStatus.OK);
    }

    @PutMapping("/estacion/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable int id, @RequestBody @Valid Station station) {
        if (id != station.getId())
            throw new InvalidStationException("Provided param /id and id on station entity do not match");
        if (!stationService.existsStation(station.getId()))
            throw new NotFoundStationException("Station not found");

        Station updatedStation = stationService.updateStation(station);
        return new ResponseEntity<>(updatedStation, HttpStatus.OK);
    }

    @DeleteMapping("/estacion/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable Integer id) {
        if (stationService.existsStation(id) == false)
            throw new NotFoundStationException("Station not found");

        stationService.deleteStationById(id);
        return new ResponseEntity<>("Station with ID " + id + " deleted", HttpStatus.OK);
    }

}
