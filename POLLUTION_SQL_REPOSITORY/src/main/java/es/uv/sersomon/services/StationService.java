package es.uv.sersomon.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Station;
import es.uv.sersomon.repositories.StationRepository;

@Service
public class StationService {
    @Autowired
    StationRepository stationRepository;

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStationById(Integer id) {
        stationRepository.deleteById(id);
    }

    public Optional<Station> findStationById(Integer id) {
        return stationRepository.findById(id);
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Station updateStation(Station station) {
        return stationRepository.save(station);
    }

    public boolean existsStation(Integer id) {
        return stationRepository.existsById(id);
    }

}
