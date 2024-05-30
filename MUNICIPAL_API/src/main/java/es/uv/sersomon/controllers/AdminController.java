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
import es.uv.sersomon.models.Station;
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
    @Value("${app.jwt.key}")
    private String key;

    @PostMapping("/estacion")
    public ResponseEntity<Station> createStation(@RequestBody @Valid Station station) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Station> request = new HttpEntity<>(station, headers);
        try {
            ResponseEntity<Station> createdStation = restTemplate.exchange(
                    stationServiceUrl + "/estacion",
                    HttpMethod.POST,
                    request,
                    Station.class);
            return new ResponseEntity<>(createdStation.getBody(), HttpStatus.OK);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(null, e.getStatusCode());
        }
    }

    @DeleteMapping("/estacion/{id}")
    public void deleteStation(@PathVariable int id) {
        restTemplate.delete(stationServiceUrl + "/estacion/" + id);
    }

    @PostMapping("/aparcamiento")
    public ResponseEntity<Parking> createParking(@RequestBody @Valid Parking parking) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Parking> request = new HttpEntity<>(parking, headers);
        try {
            ResponseEntity<Parking> createdParking = restTemplate.exchange(
                    parkingServiceUrl + "/aparcamiento",
                    HttpMethod.POST,
                    request,
                    Parking.class);
            return new ResponseEntity<>(createdParking.getBody(), HttpStatus.OK);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(null, e.getStatusCode());
        }
    }

    @DeleteMapping("/aparcamiento/{id}")
    public void deleteParking(@PathVariable int id) {
        restTemplate.delete(parkingServiceUrl + "/aparcamiento/" + id);
    }

}
