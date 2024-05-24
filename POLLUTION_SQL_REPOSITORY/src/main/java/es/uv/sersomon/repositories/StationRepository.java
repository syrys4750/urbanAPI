package es.uv.sersomon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Station;

public interface StationRepository extends JpaRepository<Station, Integer> {

}
