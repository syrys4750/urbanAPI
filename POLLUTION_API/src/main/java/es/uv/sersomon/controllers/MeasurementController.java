package es.uv.sersomon.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.models.Station;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class MeasurementController {
    @Autowired
    RestTemplate restTemplate;

    @Value("${app.repository.stations.url}")
    String stationsRepositoryUrl;

    @Value("${app.repository.measurements.url}")
    String measurementRepositoryUrl;

    @PostMapping("/estacion/{id}")
    public ResponseEntity<?> createMeasurement(@RequestBody Measurement measurement, @PathVariable int id) {
        try {
            // In case the station does not exists, an exception is thrown
            restTemplate.getForEntity(stationsRepositoryUrl + "/estaciones/" + id, Station.class);
            ResponseEntity<Measurement> response = restTemplate
                    .postForEntity(measurementRepositoryUrl + "/estacion/" + id, measurement, Measurement.class);
            return fixTransferEncodingHeader(response);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return fixTransferEncodingHeader(
                    ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
        }
    }

    @GetMapping("/estacion/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        try {
            String url = measurementRepositoryUrl + "/estacion/" + id + "/status";
            if (from != null && to != null) {
                url += "?from=" + from + "&to=" + to;
            }
            ResponseEntity<Measurement[]> response = restTemplate.getForEntity(url, Measurement[].class);
            return fixTransferEncodingHeader(ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return fixTransferEncodingHeader(
                    ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
        }
    }

    private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
        httpHeaders.set("Transfer-Encoding", null);

        return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
    }
}