package es.uv.sersomon.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.repositories.ParkingRepository;

@Service
public class ParkingService {

    @Autowired
    ParkingRepository parkingRepository;

    public Parking createParking(Parking parking) {
        return parkingRepository.save(parking);
    }

    public void deleteParking(int id) {
        parkingRepository.deleteById(id);
    }

    public List<Parking> findParkings() {
        return parkingRepository.findAll();
    }

    public Parking updateParking(int id, Parking parking) {
        if (parkingRepository.existsById(Integer.valueOf(id))) {
            parking.setId(Integer.valueOf(id));
            return parkingRepository.save(parking);
        }

        return new Parking();

    }

    public Optional<Parking> findParkingById(int id) {
        return parkingRepository.findById(id);
    }

    public boolean existsById(int id) {
        return parkingRepository.existsById(id);
    }

    public boolean isParkingAlreadyPresent(Parking parking) {
        return parkingRepository.existsByDirectionAndBikesCapacityAndLatitudeAndLongitude(parking.getDirection(),
                parking.getBikesCapacity(), parking.getLatitude(), parking.getLongitude());
    }
}
