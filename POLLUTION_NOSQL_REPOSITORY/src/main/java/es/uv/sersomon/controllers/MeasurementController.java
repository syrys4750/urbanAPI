package es.uv.sersomon.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.services.MeasurementService;
import jakarta.websocket.server.PathParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class MeasurementController {
    @Autowired
    private MeasurementService measurementService;

    @PostMapping("/estacion/{id}")
    public ResponseEntity<Measurement> createMeasurement(@RequestBody @Validated Measurement measurement) {

        Measurement measurementCreated = measurementService.createMeasurement(measurement);
        return new ResponseEntity<>(measurementCreated, HttpStatus.OK);
    }

    @GetMapping("estacion/{id}/status")
    public ResponseEntity<?> findMeasurementByStation(@PathVariable Integer id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        if (from == null || to == null) {
            Measurement latestMeasurement = measurementService.findLatestMeasurementByStation(id);
            return new ResponseEntity<>(latestMeasurement, HttpStatus.OK);
        } else {
            List<Measurement> measurementsFromTo = measurementService.findMeasurementByStationIdFromTo(id, from, to);
            return new ResponseEntity<>(measurementsFromTo, HttpStatus.OK);
        }
    }
}