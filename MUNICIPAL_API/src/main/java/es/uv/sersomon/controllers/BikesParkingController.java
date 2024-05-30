package es.uv.sersomon.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.models.AirQuality;
import es.uv.sersomon.models.Event;
import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.models.Parking;
import es.uv.sersomon.models.ParkingBikesPollution;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/v1")
public class BikesParkingController {
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.service.parking.url}")
    private String parkingServiceUrl;
    @Value("${app.service.station.url}")
    private String stationServiceUrl;

    static final double EARTH_RADIUS = 6371;

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

    /*
     * This method retrieves all available parkings and calculates the distance to
     * each one.
     * When a nearby parking is found, it checks if there are bikes available.
     * If bikes are available, this parking is marked as the nearest parking.
     *
     * An alternative approach would be to directly modify the
     * MOBILITY_NOSQL_REPOSITORY
     * and the MOBILITY_API to obtain documents sorted by timestamp without
     * repetition.
     * However, it is considered important to maintain separation of
     * responsibilities.
     * Therefore, the MUNICIPAL_API should not require modifications to another API.
     */
    private Parking getNearestAvailableParkingFromApi(Double latitude, Double longitude) {
        ResponseEntity<Parking[]> allParkingsPrimitive = restTemplate.getForEntity(parkingServiceUrl + "/aparcamientos",
                Parking[].class);
        List<Parking> allParkings = Arrays.asList(allParkingsPrimitive.getBody());
        Parking nearestParking = new Parking();
        double nearestDistance = 1000000;
        for (Parking parking : allParkings) {
            double currentDistance = calculateDistance(parking.getLatitude(), parking.getLongitude(), latitude,
                    longitude);
            if (currentDistance < nearestDistance) {
                ResponseEntity<Event> currentEvent = restTemplate
                        .getForEntity(parkingServiceUrl + "/aparcamiento/" + parking.getId() + "/status", Event.class);
                if (currentEvent.getBody().getBikesAvailable() > 0) {
                    nearestDistance = currentDistance;
                    nearestParking = parking;
                }
            }
        }

        return nearestParking;
    }

    @GetMapping("/aparcamientoCercano")
    public ResponseEntity<?> getNearestAvailableParking(
            @RequestParam(value = "lat", required = false) Double latitude,
            @RequestParam(value = "lon", required = false) Double longitude) {

        if (latitude == null || longitude == null) {
            return new ResponseEntity<>("Latitude and longitude params are required.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(getNearestAvailableParkingFromApi(latitude, longitude), HttpStatus.OK);
    }

}
