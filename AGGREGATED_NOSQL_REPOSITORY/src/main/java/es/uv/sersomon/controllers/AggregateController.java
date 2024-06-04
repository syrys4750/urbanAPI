package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.services.AggregateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/v1")
public class AggregateController {

    @Autowired
    AggregateService aggregateService;

    @PostMapping("/aggregateData")
    @Operation(summary = "Add an aggregate of information", description = "Aggregated information will be provided by the MUNICIPAL_API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregate added successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Aggregate.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more fields are null or invalid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (No ROLE_ADMIN or ROLE_SERVICE)", content = @Content) })
    public ResponseEntity<Aggregate> addAggregate(@RequestBody @Valid Aggregate aggregated) {
        Aggregate createdAggregate = aggregateService.addAggregate(aggregated);
        return new ResponseEntity<>(createdAggregate, HttpStatus.OK);
    }

    @GetMapping("/aggregatedData")
    @Operation(summary = "Get latest aggregated data", description = "Retrieve the most recent aggregated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Latest aggregated data retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Aggregate.class)) }) })
    public ResponseEntity<Aggregate> getLatestAggregated() {
        return new ResponseEntity<>(aggregateService.getLatestAggregated(), HttpStatus.OK);
    }

}
