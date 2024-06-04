package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.models.ParkingToken;
import es.uv.sersomon.models.Station;
import es.uv.sersomon.models.StationToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/v1")
public class AdminController {
    @Autowired
    RestTemplate restTemplate;

    @Value("${app.service.parking.url}")
    private String parkingServiceUrl;

    @Value("${app.service.station.url}")
    private String stationServiceUrl;

    @Value("${app.repository.auth.url}")
    String authServiceUrl;

    @Value("${app.jwt.key}")
    private String key;

    @PostMapping("/estacion")
    @Operation(summary = "Create Station", description = "Creates a new station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StationToken.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, station already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_SERVICE)", content = @Content)
    })
    public ResponseEntity<StationToken> createStation(@RequestBody @Valid Station station) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Station> request = new HttpEntity<>(station, headers);
        try {
            ResponseEntity<StationToken> createdStation = restTemplate.exchange(
                    stationServiceUrl + "/estacion",
                    HttpMethod.POST,
                    request,
                    StationToken.class);
            return fixTransferEncodingHeader(new ResponseEntity<>(createdStation.getBody(), HttpStatus.OK));
        } catch (HttpStatusCodeException e) {
            return fixTransferEncodingHeader(new ResponseEntity<>(null, e.getStatusCode()));
        }
    }

    @DeleteMapping("/estacion/{id}")
    @Operation(summary = "Delete Station", description = "Deletes a station by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Station deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_SERVICE)", content = @Content)
    })
    public ResponseEntity<String> deleteStation(@PathVariable int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<?> request = new HttpEntity<Object>(headers);
        restTemplate.exchange(stationServiceUrl + "/estacion/" + id, HttpMethod.DELETE, request, String.class);
        return new ResponseEntity<>("Station with id " + id + " marked for deletion", HttpStatus.OK);
    }

    @PostMapping("/aparcamiento")
    @Operation(summary = "Create Parking", description = "Creates a new parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingToken.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, parking already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_SERVICE)", content = @Content)
    })
    public ResponseEntity<ParkingToken> createParking(@RequestBody @Valid Parking parking) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Parking> request = new HttpEntity<>(parking, headers);
        try {
            ResponseEntity<ParkingToken> createdParkingToken = restTemplate.exchange(
                    parkingServiceUrl + "/aparcamiento",
                    HttpMethod.POST,
                    request,
                    ParkingToken.class);

            return fixTransferEncodingHeader(createdParkingToken);
        } catch (HttpStatusCodeException e) {
            return fixTransferEncodingHeader(new ResponseEntity<>(null, e.getStatusCode()));
        }
    }

    @DeleteMapping("/aparcamiento/{id}")
    @Operation(summary = "Delete Parking", description = "Deletes a parking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_SERVICE)", content = @Content)
    })
    public ResponseEntity<String> deleteParking(@PathVariable int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<?> request = new HttpEntity<Object>(headers);
        restTemplate.exchange(parkingServiceUrl + "/aparcamiento/" + id, HttpMethod.DELETE, request, String.class);
        return new ResponseEntity<>("Parking with id " + id + " marked for deletion", HttpStatus.OK);
    }

    private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
        httpHeaders.set("Transfer-Encoding", null);

        return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
    }
}
