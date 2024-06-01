package es.uv.sersomon.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.events.Event;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.services.ParkingService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class ParkingController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ParkingController.class);

    @Autowired
    ParkingService parkingService;

    // TODO: devolver error si todos los campos son vacios?
    @PostMapping("/aparcamiento")
    public ResponseEntity<Parking> createParking(@RequestBody Parking parkingRequest) {
        LOGGER.debug("Create Parking with ID " + parkingRequest.getId());
        if (parkingService.isParkingAlreadyPresent(parkingRequest)) {
            return new ResponseEntity<Parking>(new Parking(), HttpStatus.CONFLICT);
        }
        Parking parking = parkingService.createParking(parkingRequest);

        return new ResponseEntity<Parking>(parking, HttpStatus.OK);
    }

    @DeleteMapping("/aparcamiento/{id}")
    public void deleteParkingById(@PathVariable("id") int id) {
        LOGGER.debug("Delete Parking with ID " + id);
        parkingService.deleteParking(id);
    }

    @PutMapping("aparcamiento/{id}")
    public ResponseEntity<Parking> updateParkingById(@PathVariable int id, @RequestBody Parking parking) {
        Parking savedParking = parkingService.updateParking(id, parking);
        if (savedParking.getId() == null) {
            return new ResponseEntity<Parking>(new Parking(), HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<Parking>(savedParking, HttpStatus.ACCEPTED);
    }

    @GetMapping("/aparcamientos")
    public ResponseEntity<List<Parking>> findParkings() {
        LOGGER.debug("Find all parkings");

        return new ResponseEntity<List<Parking>>(parkingService.findParkings(), HttpStatus.OK);
    }

    @GetMapping("/aparcamientos/{id}")
    public ResponseEntity<Parking> findParkingById(@PathVariable int id) {
        LOGGER.debug("Find parking by ID: " + id);
        Optional<Parking> parking = parkingService.findParkingById(id);
        if (parking.isPresent())
            return new ResponseEntity<Parking>(parking.get(), HttpStatus.OK);
        return new ResponseEntity<Parking>(new Parking(), HttpStatus.NOT_FOUND);
    }

}
