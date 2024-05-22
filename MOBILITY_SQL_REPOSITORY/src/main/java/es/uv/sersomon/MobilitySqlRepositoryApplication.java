package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.services.ParkingService;

@SpringBootApplication
public class MobilitySqlRepositoryApplication {

	@Autowired
	ParkingService parkingService;

	public static void main(String[] args) {
		SpringApplication.run(MobilitySqlRepositoryApplication.class, args);
	}

}
