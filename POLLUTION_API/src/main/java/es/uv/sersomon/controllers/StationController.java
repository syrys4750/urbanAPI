package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Station;
import es.uv.sersomon.models.StationToken;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1")
public class StationController {
        @Autowired
        RestTemplate restTemplate;

        @Value("${app.repository.stations.url}")
        String stationRepositoryUrl;

        @Value("${app.repository.auth.url}")
        String authServiceUrl;

        private static final String ROLE_STATION = "ROLE_STATION";

        @PostMapping("/estacion")
        @Operation(summary = "Create Station", description = "Creates a new station and generates a JWT token for it")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Station created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = StationToken.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
        })
        public ResponseEntity<?> createStation(@RequestBody @Validated Station station) {
                try {
                        ResponseEntity<Station> response = restTemplate.postForEntity(
                                        stationRepositoryUrl + "/estacion", station,
                                        Station.class);
                        ResponseEntity<String> jwtTokenForParking = restTemplate.getForEntity(
                                        authServiceUrl + "/generateJwtToken?id=" + response.getBody().getId() + "&role="
                                                        + ROLE_STATION,
                                        String.class);
                        ResponseEntity<StationToken> responseToken = new ResponseEntity<>(
                                        new StationToken(response.getBody(), jwtTokenForParking.getBody()),
                                        HttpStatus.OK);
                        return fixTransferEncodingHeader(responseToken);
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
                }
        }

        @DeleteMapping("/estacion/{id}")
        @Operation(summary = "Delete Station", description = "Deletes a station by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Station deleted successfully", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
        })
        public ResponseEntity<String> deleteStation(@PathVariable int id) {
                try {
                        restTemplate.delete(stationRepositoryUrl + "/estacion/" + id);
                        return fixTransferEncodingHeader(ResponseEntity.noContent().build());
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
                }
        }

        @GetMapping("/estaciones")
        @Operation(summary = "Find All Stations", description = "Retrieves all stations")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stations retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Station[].class)) }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        public ResponseEntity<Station[]> findAllStations() {
                ResponseEntity<Station[]> response = restTemplate.getForEntity(stationRepositoryUrl + "/estaciones",
                                Station[].class);
                return fixTransferEncodingHeader(response);
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
        public ResponseEntity<?> updateStation(@PathVariable int id, @RequestBody @Valid Station station) {
                try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        station.setId(Integer.valueOf(id));
                        HttpEntity<Station> entity = new HttpEntity<>(station, headers);

                        ResponseEntity<Station> response = restTemplate.exchange(
                                        stationRepositoryUrl + "/estacion/" + id,
                                        HttpMethod.PUT,
                                        entity,
                                        Station.class);

                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
                } catch (Exception ex) {
                        return fixTransferEncodingHeader(ResponseEntity.status(500).body("Internal Server Error"));
                }
        }

        private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
                HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
                httpHeaders.set("Transfer-Encoding", null);

                return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
        }
}
