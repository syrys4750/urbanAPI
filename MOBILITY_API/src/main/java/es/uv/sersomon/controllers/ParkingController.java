package es.uv.sersomon.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.DTOs.StatusDTO;
import es.uv.sersomon.models.Event;
import es.uv.sersomon.models.Parking;
import es.uv.sersomon.models.ParkingToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequestMapping("/api/v1")
public class ParkingController {

        @Autowired
        RestTemplate restTemplate;

        @Value("${app.repository.parkings.url}")
        String parkingRepositoryUrl;

        @Value("${app.repository.events.url}")
        String eventRepositoryUrl;

        @Value("${app.repository.auth.url}")
        String authServiceUrl;

        private static final String ROLE_PARKING = "ROLE_PARKING";

        @PostMapping("/aparcamiento")
        @Operation(summary = "Create Parking", description = "Creates a new parking and generates a JWT token for it")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Parking created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingToken.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden (user has no ROLE_ADMIN)", content = @Content)
        })
        public ResponseEntity<ParkingToken> createParking(@RequestBody @Valid Parking parking) {
                try {
                        ResponseEntity<Parking> response = restTemplate.postForEntity(
                                        parkingRepositoryUrl + "/aparcamiento",
                                        parking, Parking.class);
                        ResponseEntity<String> jwtTokenForParking = restTemplate.getForEntity(
                                        authServiceUrl + "/generateJwtToken?id=" + response.getBody().getId() + "&role="
                                                        + ROLE_PARKING,
                                        String.class);
                        ResponseEntity<ParkingToken> responseToken = new ResponseEntity<ParkingToken>(
                                        new ParkingToken(response.getBody(), jwtTokenForParking.getBody()),
                                        HttpStatus.OK);
                        return fixTransferEncodingHeader(responseToken);
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(ex.getStatusCode())
                                                        .body(ex.getResponseBodyAs(ParkingToken.class)));
                }
        }

        @DeleteMapping("/aparcamiento/{id}")
        @Operation(summary = "Delete Parking", description = "Deletes a parking by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Parking deleted successfully", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
        })
        public ResponseEntity<String> deleteParking(@PathVariable int id) {
                restTemplate.delete(parkingRepositoryUrl + "/aparcamiento/" + id);
                return new ResponseEntity<>("Parking with id " + id + " marked for deletion", HttpStatus.OK);
        }

        @GetMapping("/aparcamientos")
        @Operation(summary = "Find All Parkings", description = "Retrieves all parkings")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Parkings retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Parking[].class)) })
        })
        public ResponseEntity<Parking[]> findAllParkings() {
                ResponseEntity<Parking[]> response = restTemplate.getForEntity(parkingRepositoryUrl + "/aparcamientos",
                                Parking[].class);
                return fixTransferEncodingHeader(response);
        }

        @PutMapping("aparcamiento/{id}")
        @Operation(summary = "Update Parking", description = "Updates a parking by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Parking updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden (user has no ROLE_ADMIN)", content = @Content)
        })
        public ResponseEntity<Parking> updateParking(@PathVariable String id, @RequestBody @Valid Parking parking) {
                try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<Parking> entity = new HttpEntity<>(parking, headers);

                        ResponseEntity<Parking> response = restTemplate.exchange(
                                        parkingRepositoryUrl + "/aparcamiento/" + id,
                                        HttpMethod.PUT,
                                        entity,
                                        Parking.class);

                        return fixTransferEncodingHeader(
                                        ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                        return fixTransferEncodingHeader(ResponseEntity.status(ex.getStatusCode()).body(null));
                }
        }

        @GetMapping("/aparcamiento/{id}/status")
        @Operation(summary = "Find Parking Status", description = "Retrieves the status of a parking by its ID and optionally within a date range")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Status retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = StatusDTO[].class)) }),
                        @ApiResponse(responseCode = "404", description = "Parking or status not found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "from date is after to date", content = @Content)
        })
        public ResponseEntity<?> findParkingStatus(@PathVariable int id,
                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
                String requestUrl = eventRepositoryUrl + "/aparcamiento/" + id + "/status";

                ResponseEntity<?> response;
                try {
                        if (from != null && to != null) {
                                requestUrl = requestUrl + "?from=" + from + "&to=" + to;
                                response = restTemplate.getForEntity(requestUrl, StatusDTO[].class);
                        } else {
                                response = restTemplate.getForEntity(requestUrl, StatusDTO.class);
                        }

                        if (from != null && to != null && from.isAfter(to)) {
                                response = ResponseEntity.badRequest().body("Bad request: from date is after to date");
                        }
                } catch (HttpClientErrorException.NotFound e) {
                        // Manejo de la excepción 404
                        response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking not found");
                }

                return fixTransferEncodingHeader(response);
        }

        @GetMapping("/top10")
        @Operation(summary = "Get Top 10 Parkings", description = "Retrieves the top 10 parkings by availability")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Top 10 parkings retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) })
        })
        public ResponseEntity<List<Parking>> getTop10ParkingsNow() {
                ResponseEntity<List> top10ParkingsIdByDisponibility = restTemplate
                                .getForEntity(eventRepositoryUrl + "/top10", List.class);
                List<Parking> top10ParkingsByDisponibility = new ArrayList<>();
                for (Object parkingId : top10ParkingsIdByDisponibility.getBody()) {
                        Integer parkingIdInteger = Integer.valueOf(parkingId.toString());
                        ResponseEntity<Parking> parkingResponse = restTemplate
                                        .getForEntity(parkingRepositoryUrl + "/aparcamientos/"
                                                        + parkingIdInteger.intValue(),
                                                        Parking.class);
                        if (parkingResponse.getStatusCode() != HttpStatus.OK
                                        && parkingResponse.getStatusCode() != HttpStatus.ACCEPTED)
                                return fixTransferEncodingHeader(
                                                new ResponseEntity<>(new ArrayList<>(),
                                                                parkingResponse.getStatusCode()));
                        top10ParkingsByDisponibility.add(parkingResponse.getBody());
                }
                return fixTransferEncodingHeader(new ResponseEntity<>(top10ParkingsByDisponibility, HttpStatus.OK));
        }

        private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
                HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
                httpHeaders.set("Transfer-Encoding", null);

                return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
        }
}
