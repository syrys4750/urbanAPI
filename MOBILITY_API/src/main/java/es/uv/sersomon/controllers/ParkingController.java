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
    public ResponseEntity<ParkingToken> createParking(@RequestBody @Valid Parking parking) {
        try {
            ResponseEntity<Parking> response = restTemplate.postForEntity(parkingRepositoryUrl + "/aparcamiento",
                    parking, Parking.class);
            ResponseEntity<String> jwtTokenForParking = restTemplate.getForEntity(
                    authServiceUrl + "/generateJwtToken?id=" + response.getBody().getId() + "&role=" + ROLE_PARKING,
                    String.class);
            ResponseEntity<ParkingToken> responseToken = new ResponseEntity<ParkingToken>(
                    new ParkingToken(response.getBody(), jwtTokenForParking.getBody()), HttpStatus.OK);
            return fixTransferEncodingHeader(responseToken);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return fixTransferEncodingHeader(
                    ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAs(ParkingToken.class)));
        }
    }

    @DeleteMapping("/aparcamiento/{id}")
    public ResponseEntity<String> deleteParking(@PathVariable int id) {
        restTemplate.delete(parkingRepositoryUrl + "/aparcamiento/" + id);
        return new ResponseEntity<>("Parking with id " + id + " marked for delete", HttpStatus.OK);
    }

    @GetMapping("/aparcamientos")
    public ResponseEntity<Parking[]> findAllParkings() {
        ResponseEntity<Parking[]> response = restTemplate.getForEntity(parkingRepositoryUrl + "/aparcamientos",
                Parking[].class);
        return fixTransferEncodingHeader(response);
    }

    @PutMapping("aparcamiento/{id}")
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

            return fixTransferEncodingHeader(ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return fixTransferEncodingHeader(ResponseEntity.status(ex.getStatusCode()).body(null));
        }
    }

    @GetMapping("/aparcamiento/{id}/status")
    public ResponseEntity<?> findParkingStatus(@PathVariable int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        String requestUrl = eventRepositoryUrl + "/aparcamiento/" + id + "/status";

        ResponseEntity<?> response;
        if (from != null && to != null) {
            requestUrl = requestUrl + "?from=" + from + "&to=" + to;
            response = restTemplate.getForEntity(requestUrl, StatusDTO[].class);
        } else {
            response = restTemplate.getForEntity(requestUrl, StatusDTO.class);
        }

        return fixTransferEncodingHeader(response);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Parking>> getTop10ParkingsNow() {
        ResponseEntity<List> top10ParkingsIdByDisponibility = restTemplate
                .getForEntity(eventRepositoryUrl + "/top10", List.class);
        List<Parking> top10ParkingsByDisponibility = new ArrayList<>();
        for (Object parkingId : top10ParkingsIdByDisponibility.getBody()) {
            Integer parkingIdInteger = Integer.valueOf(parkingId.toString());
            ResponseEntity<Parking> parkingResponse = restTemplate
                    .getForEntity(parkingRepositoryUrl + "/aparcamientos/" + parkingIdInteger.intValue(),
                            Parking.class);
            if (parkingResponse.getStatusCode() != HttpStatus.OK
                    && parkingResponse.getStatusCode() != HttpStatus.ACCEPTED)
                return fixTransferEncodingHeader(
                        new ResponseEntity<>(new ArrayList<>(), parkingResponse.getStatusCode()));
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
