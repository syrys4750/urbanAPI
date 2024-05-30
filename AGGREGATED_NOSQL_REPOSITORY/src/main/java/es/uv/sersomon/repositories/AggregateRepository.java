package es.uv.sersomon.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.uv.sersomon.models.Aggregate;

public interface AggregateRepository extends MongoRepository<Aggregate, String> {

    Aggregate findFirstByOrderByTimeStampDesc();

}