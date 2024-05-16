package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.services.ParkingService;

@SpringBootApplication
public class MobilitySqlRepositoryApplication implements CommandLineRunner {

	@Autowired
	ParkingService parkingService;

	public static void main(String[] args) {
		SpringApplication.run(MobilitySqlRepositoryApplication.class, args);
	}

	// TODO: cargar la información a partir de un fichero? formato del fichero?
	// TODO: como garantizo que no voy a sobreescribir informacion?
	@Override
	public void run(String... args) throws Exception {
		// Crear y guardar objetos Parking
		parkingService
				.createParking(new Parking(1, "1500 Amphitheatre Parkway, Mountain View, CA", 25, 37.4220, -122.0841));
		parkingService.createParking(new Parking(2, "One Apple Park Way, Cupertino, CA", 50, 37.3349, -122.0090));
		parkingService.createParking(new Parking(3, "Empire State Building, New York, NY", 75, 40.7488, -73.9854));
	}

}
