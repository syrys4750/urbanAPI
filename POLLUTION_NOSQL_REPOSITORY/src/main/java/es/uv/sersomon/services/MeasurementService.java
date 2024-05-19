package es.uv.sersomon.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.repositories.MeasurementRepository;

@Service
public class MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;

    public Measurement createMeasurement(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    public Measurement findLatestMeasurementByStation(Integer id) {
        return measurementRepository.findFirstByIdStationOrderByTimestampDesc(id);
    }

    public List<Measurement> findMeasurementByStationIdFromTo(Integer id, LocalDateTime from, LocalDateTime to) {
        return measurementRepository.findByIdStationAndTimestampBetweenOrderByTimestampDesc(id, from, to);
    }

}
