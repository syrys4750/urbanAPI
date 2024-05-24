package es.uv.sersomon.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Measurement;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class MeasurementController {
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.repository.measurements.url}")
    String measurementRepositoryUrl;

    @PostMapping("/estacion/{id}")
    public ResponseEntity<Measurement> createMeasurement(@RequestBody Measurement measurement, @PathVariable int id) {
        return restTemplate.postForEntity(measurementRepositoryUrl + "/estacion/{id}", measurement, Measurement.class);
    }

    @GetMapping("/estacion/{id}/status")
    public ResponseEntity<Measurement> getStatus(@PathVariable int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        if (from != null && to != null) {
            return restTemplate.getForEntity(
                    measurementRepositoryUrl + "/estacion/" + id + "/status?from=" + from + "&to=" + to,
                    Measurement.class);
        } else
            return restTemplate.getForEntity(measurementRepositoryUrl + "/estacion/" + id + "/status",
                    Measurement.class);
    }

}
