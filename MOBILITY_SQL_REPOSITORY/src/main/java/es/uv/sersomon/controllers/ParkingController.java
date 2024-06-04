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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class ParkingController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ParkingController.class);

    @Autowired
    ParkingService parkingService;

    @PostMapping("/aparcamiento")
    @Operation(summary = "Create Parking", description = "Creates a new parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict, parking already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_PARKING)", content = @Content)
    })
    public ResponseEntity<Parking> createParking(@RequestBody @Valid Parking parkingRequest) {
        LOGGER.debug("Create Parking with ID " + parkingRequest.getId());
        if (parkingService.isParkingAlreadyPresent(parkingRequest)) {
            return new ResponseEntity<Parking>(new Parking(), HttpStatus.CONFLICT);
        }
        parkingRequest.setId(null); // ensure that no parking is overwritten even if ID is passed
        Parking parking = parkingService.createParking(parkingRequest);

        return new ResponseEntity<Parking>(parking, HttpStatus.OK);
    }

    @DeleteMapping("/aparcamiento/{id}")
    @Operation(summary = "Delete Parking", description = "Deletes a parking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
    })
    public ResponseEntity<String> deleteParkingById(@PathVariable("id") int id) {
        LOGGER.debug("Delete Parking with ID " + id);
        parkingService.deleteParking(id);
        return new ResponseEntity<>("Parking with id " + id + " marked for deletion", HttpStatus.OK);
    }

    @PutMapping("aparcamiento/{id}")
    @Operation(summary = "Update Parking", description = "Updates a parking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Parking updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN)", content = @Content)
    })
    public ResponseEntity<Parking> updateParkingById(@PathVariable int id, @RequestBody @Valid Parking parking) {
        Parking savedParking = parkingService.updateParking(id, parking);
        if (savedParking.getId() == null) {
            return new ResponseEntity<Parking>(new Parking(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Parking>(savedParking, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/aparcamientos")
    @Operation(summary = "Find All Parkings", description = "Retrieves all parkings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parkings retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) })
    })
    public ResponseEntity<List<Parking>> findParkings() {
        LOGGER.debug("Find all parkings");
        return new ResponseEntity<List<Parking>>(parkingService.findParkings(), HttpStatus.OK);
    }

    @GetMapping("/aparcamientos/{id}")
    @Operation(summary = "Find Parking by ID", description = "Retrieves a parking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Parking.class)) }),
            @ApiResponse(responseCode = "404", description = "Parking not found", content = @Content)
    })
    public ResponseEntity<Parking> findParkingById(@PathVariable int id) {
        LOGGER.debug("Find parking by ID: " + id);
        Optional<Parking> parking = parkingService.findParkingById(id);
        if (parking.isPresent()) {
            return new ResponseEntity<Parking>(parking.get(), HttpStatus.OK);
        }
        return new ResponseEntity<Parking>(new Parking(), HttpStatus.NOT_FOUND);
    }

}
