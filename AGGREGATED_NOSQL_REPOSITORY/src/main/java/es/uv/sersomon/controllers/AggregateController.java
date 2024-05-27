package es.uv.sersomon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.services.AggregateService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1")
public class AggregateController {
    @Autowired
    AggregateService aggregateService;

    @PostMapping("/aggregateData")
    public ResponseEntity<Aggregate> addAggregate(@RequestBody @Valid Aggregate aggregated) {
        Aggregate createdAggregate = aggregateService.addAggregate(aggregated);

        return new ResponseEntity<>(createdAggregate, HttpStatus.OK);
    }
}