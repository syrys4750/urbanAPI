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

    private static final String ROLE_PARKING = "ROLE_PARKING";

    @PostMapping("/estacion")
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
    public ResponseEntity<String> deleteStation(@PathVariable int id) {
        restTemplate.delete(stationServiceUrl + "/estacion/" + id);
        return new ResponseEntity<>("Station with id " + id + " marked for delete", HttpStatus.OK);
    }

    @PostMapping("/aparcamiento")
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
    public ResponseEntity<String> deleteParking(@PathVariable int id) {
        restTemplate.delete(parkingServiceUrl + "/aparcamiento/" + id);
        return new ResponseEntity<>("Station with id " + id + " marked for delete", HttpStatus.OK);
    }

    private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
        httpHeaders.set("Transfer-Encoding", null);

        return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
    }
}