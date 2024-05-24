package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Station;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class StationController {
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.repository.stations.url}")
    String stationRepositoryUrl;

    @PostMapping("/estacion")
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        return restTemplate.postForEntity(stationRepositoryUrl + "/estacion", station, Station.class);
    }

    @DeleteMapping("/estacion/{id}")
    public void deleteStation(@PathVariable int id) {
        restTemplate.delete(stationRepositoryUrl + "/estacion/" + id);
    }

    @GetMapping("/estaciones")
    public ResponseEntity<Station[]> findAllStations() {
        return restTemplate.getForEntity(stationRepositoryUrl + "/estaciones", Station[].class);
    }

}
