package es.uv.sersomon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uv.sersomon.models.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Integer> {

}
