package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Station;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<?> createStation(@RequestBody @Validated Station station) {
        try {
            ResponseEntity<Station> response = restTemplate.postForEntity(stationRepositoryUrl + "/estacion", station,
                    Station.class);
            return fixTransferEncodingHeader(response);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return fixTransferEncodingHeader(
                    ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
        }
    }

    @DeleteMapping("/estacion/{id}")
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
    public ResponseEntity<Station[]> findAllStations() {
        ResponseEntity<Station[]> response = restTemplate.getForEntity(stationRepositoryUrl + "/estaciones",
                Station[].class);
        return fixTransferEncodingHeader(response);
    }

    @PutMapping("/estacion/{id}")
    public ResponseEntity<?> updateStation(@PathVariable int id, @RequestBody @Validated Station station) {
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

            return fixTransferEncodingHeader(ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
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
