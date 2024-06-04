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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Operation(summary = "Create Measurement", description = "Creates a new measurement for a specified station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurement created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Measurement.class)) }),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_STATION)", content = @Content)
    })
    public ResponseEntity<?> createMeasurement(@RequestBody @Valid Measurement measurement, @PathVariable int id) {
        try {
            // In case the station does not exist, an exception is thrown
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
    @Operation(summary = "Get Station Status", description = "Retrieves the status of a station's measurements within an optional date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Measurement[].class)) }),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content)
    })
    public ResponseEntity<?> getStatus(@PathVariable int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        try {
            String url = measurementRepositoryUrl + "/estacion/" + id + "/status";
            ResponseEntity<?> response;
            if (from != null && to != null) {
                url += "?from=" + from + "&to=" + to;
            }
            response = restTemplate.getForEntity(url, Measurement[].class);
            if (from != null && to != null && from.isAfter(to)) {
                response = ResponseEntity.badRequest().body("Bad request: from date is after to date");
            }
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
