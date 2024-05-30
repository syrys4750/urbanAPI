package es.uv.sersomon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.repositories.AggregateRepository;
import jakarta.validation.Valid;

@Service
public class AggregateService {

    @Autowired
    AggregateRepository aggregateRepository;

    public Aggregate addAggregate(Aggregate aggregated) {
        return aggregateRepository.insert(aggregated);
    }

    public Aggregate getLatestAggregated() {

        return aggregateRepository.findFirstByOrderByTimeStampDesc();
    }

}
