package es.uv.sersomon.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Event;
import es.uv.sersomon.models.Parking;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1")
public class ParkingController {
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.repository.parkings.url}")
    String parkingRepositoryUrl;
    @Value("${app.repository.events.url}")
    String eventRepositoryUrl;

    @PostMapping("/aparcamiento")
    public ResponseEntity<Parking> createParking(@RequestBody @Validated Parking parking) {
        ResponseEntity<Parking> responseEntityParking = restTemplate
                .postForEntity(parkingRepositoryUrl + "/aparcamiento", parking, Parking.class);
        return responseEntityParking;
    }

    @DeleteMapping("/aparcamiento/{id}")
    public void deleteParking(@PathVariable int id) {
        restTemplate.delete(parkingRepositoryUrl + "/aparcamiento/" + id);
    }

    @GetMapping("/aparcamientos")
    public ResponseEntity<Parking[]> findAllParkings() {
        return restTemplate.getForEntity(parkingRepositoryUrl + "/aparcamientos", Parking[].class);
    }

    @GetMapping("/aparcamiento/{id}/status")
    public ResponseEntity<?> findParkingStatus(@PathVariable int id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        String requestUrl = eventRepositoryUrl + "/aparcamiento/" + id + "/status";

        if (from != null && to != null) {
            requestUrl = requestUrl + "?from=" + from + "&to=" + to;
            return restTemplate.getForEntity(requestUrl, Event[].class);
        }
        return restTemplate.getForEntity(requestUrl, Event.class);
    }

    @GetMapping("/top10AparcamientosDisponibles")
    public ResponseEntity<List<Parking>> getTop10ParkingsNow() {
        ResponseEntity<List> top10ParkingsIdByDisponibility = restTemplate
                .getForEntity(eventRepositoryUrl + "/top10AparcamientosDisponibles", List.class);
        List<Parking> top10ParkingsByDisponibility = new ArrayList<>();
        for (Object parkingId : top10ParkingsIdByDisponibility.getBody()) {
            Integer parkingIdInteger = Integer.valueOf(parkingId.toString());
            ResponseEntity<Parking> parkingResponse = restTemplate
                    .getForEntity(parkingRepositoryUrl + "/aparcamientos/" + parkingIdInteger.intValue(),
                            Parking.class);
            if (parkingResponse.getStatusCode() != HttpStatus.OK
                    && parkingResponse.getStatusCode() != HttpStatus.ACCEPTED)
                return new ResponseEntity<>(new ArrayList<>(), parkingResponse.getStatusCode());
            top10ParkingsByDisponibility.add(parkingResponse.getBody());
        }
        return new ResponseEntity<>(top10ParkingsByDisponibility, HttpStatus.OK);
    }

}
