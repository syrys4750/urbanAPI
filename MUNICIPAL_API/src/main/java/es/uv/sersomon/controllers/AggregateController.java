package es.uv.sersomon.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.models.AirQuality;
import es.uv.sersomon.models.Event;
import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.models.Parking;
import es.uv.sersomon.models.ParkingBikesPollution;
import es.uv.sersomon.models.Station;

@Controller
@RequestMapping("/api/v1")
public class AggregateController {
        @Autowired
        RestTemplate restTemplate;

        @Value("${app.service.parking.url}")
        private String parkingServiceUrl;

        @Value("${app.service.station.url}")
        private String stationServiceUrl;

        @Value("${app.service.aggregate.nosql.url}")
        private String aggregateServiceUrl;

        static final double EARTH_RADIUS = 6371;

        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        private double haversine(double val) {
                return Math.pow(Math.sin(val / 2), 2);
        }

        private double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
                double dLat = Math.toRadians((endLat - startLat));
                double dLong = Math.toRadians((endLong - startLong));

                startLat = Math.toRadians(startLat);
                endLat = Math.toRadians(endLat);

                double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                return EARTH_RADIUS * c;
        }

        private Station getNearestAvailableStationFromApi(Double latitude, Double longitude) {
                ResponseEntity<Station[]> allStationsPrimitive = restTemplate.getForEntity(
                                stationServiceUrl + "/estaciones",
                                Station[].class);
                List<Station> allStations = Arrays.asList(allStationsPrimitive.getBody());
                Station nearestStation = new Station();
                double nearestDistance = 1000000;
                for (Station station : allStations) {
                        double currentDistance = calculateDistance(station.getLatitude(), station.getLongitude(),
                                        latitude,
                                        longitude);
                        if (currentDistance < nearestDistance) {
                                nearestDistance = currentDistance;
                                nearestStation = station;
                        }
                }

                return nearestStation;
        }

        @GetMapping("/aggregateData")
        public ResponseEntity<Aggregate> getAggregateData() {
                ResponseEntity<Parking[]> allParkingsPrimitive = restTemplate.getForEntity(
                                parkingServiceUrl + "/aparcamientos",
                                Parking[].class);
                List<Parking> allParkings = Arrays.asList(allParkingsPrimitive.getBody());
                LocalDateTime fromDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0); // Starting date to get all events
                LocalDateTime toDate = LocalDateTime.now();
                List<ParkingBikesPollution> parkingBikesPollutions = new ArrayList<>();
                for (Parking parking : allParkings) {
                        String urlParking = String.format("%s/%d/status?from=%s&to=%s",
                                        parkingServiceUrl + "/aparcamiento",
                                        parking.getId(),
                                        fromDate.format(formatter),
                                        toDate.format(formatter));
                        ResponseEntity<Event[]> currentParkingEventsPrimitive = restTemplate.getForEntity(urlParking,
                                        Event[].class);
                        List<Event> currentParkingEvents = Arrays.asList(currentParkingEventsPrimitive.getBody());
                        Double currentParkingAverageBikesAvailable = currentParkingEvents.stream()
                                        .mapToDouble(Event::getBikesAvailable).average().orElse(0);

                        Station nearestStationToParking = getNearestAvailableStationFromApi(parking.getLatitude(),
                                        parking.getLongitude());
                        String urlStation = String.format("%s/%d/status?from=%s&to=%s",
                                        stationServiceUrl + "/estacion",
                                        nearestStationToParking.getId(),
                                        fromDate.format(formatter),
                                        toDate.format(formatter));
                        ResponseEntity<Measurement[]> currentParkingPollutionPrimitive = restTemplate.getForEntity(
                                        urlStation,
                                        Measurement[].class);
                        List<Measurement> currentParkingPollution = Arrays
                                        .asList(currentParkingPollutionPrimitive.getBody());
                        Double nitricOxidesMean = currentParkingPollution.stream()
                                        .mapToDouble(Measurement::getNitricOxides)
                                        .average().orElse(0);
                        Double nitrogenDioxidesMean = currentParkingPollution.stream()
                                        .mapToDouble(Measurement::getNitrogenDioxides)
                                        .average().orElse(0);
                        Double vocs_nmhcMean = currentParkingPollution.stream().mapToDouble(Measurement::getVocs_nmhc)
                                        .average()
                                        .orElse(0);
                        Double pm2_5Mean = currentParkingPollution.stream().mapToDouble(Measurement::getPm2_5).average()
                                        .orElse(0);
                        AirQuality currentParkingAirQuality = new AirQuality(nitricOxidesMean, nitrogenDioxidesMean,
                                        vocs_nmhcMean,
                                        pm2_5Mean);

                        ParkingBikesPollution parkingBikesPollution = new ParkingBikesPollution(parking.getId(),
                                        currentParkingAverageBikesAvailable, currentParkingAirQuality);

                        parkingBikesPollutions.add(parkingBikesPollution);
                }
                Aggregate aggregate = new Aggregate(LocalDateTime.now(), parkingBikesPollutions);
                ResponseEntity<Aggregate> createdAggregate = restTemplate
                                .postForEntity(aggregateServiceUrl + "/aggregateData", aggregate, Aggregate.class);
                return fixTransferEncodingHeader(new ResponseEntity<>(createdAggregate.getBody(), HttpStatus.OK));
        }

        @GetMapping("/aggregatedData")
        public ResponseEntity<Aggregate> getLatestAggregate() {
                ResponseEntity<Aggregate> createdAggregate = restTemplate
                                .getForEntity(aggregateServiceUrl + "/aggregatedData", Aggregate.class);

                return fixTransferEncodingHeader(new ResponseEntity<>(createdAggregate.getBody(), HttpStatus.OK));
        }

        private <T> ResponseEntity<T> fixTransferEncodingHeader(ResponseEntity<T> response) {
                HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
                httpHeaders.set("Transfer-Encoding", null);

                return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
        }
}