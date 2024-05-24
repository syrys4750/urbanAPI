package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Event;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class EventController {
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.repository.events.url}")
    String eventRepositoryUrl;

    @PostMapping("/evento/{id}")
    public ResponseEntity<Event> createEvent(@RequestBody @Validated Event event, @PathVariable int id) {
        return restTemplate.postForEntity(eventRepositoryUrl + "/aparcamiento", event, Event.class);
    }

}
