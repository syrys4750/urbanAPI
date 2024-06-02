package es.uv.sersomon.controllers;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Event;
import es.uv.sersomon.models.Parking;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class EventController {
    @Autowired
    RestTemplate restTemplate;

    @Value("${app.repository.parkings.url}")
    String parkingRepositoryUrl;

    @Value("${app.repository.events.url}")
    String eventRepositoryUrl;

    @PostMapping("/evento/{id}")
    @Operation(summary = "Add an event", description = "Aggregated will be provided by the MUNICIPAL_API")
    public ResponseEntity<?> createEvent(@RequestBody @Valid Event event, @PathVariable int id) {
        try {
            // if parking does not exist, an exception will be thrown
            restTemplate.getForEntity(parkingRepositoryUrl + "/aparcamientos/" + id, Parking.class);
            ResponseEntity<Event> response = restTemplate.postForEntity(eventRepositoryUrl + "/evento/" + id, event,
                    Event.class);
            return fixTransferEncodingHeader(response);
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
