package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.service.JwtService;

@SpringBootApplication
public class AuthApiApplication implements CommandLineRunner {
	@Autowired
	JwtService jwtService;

	public static void main(String[] args) {
		SpringApplication.run(AuthApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("ADMIN JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_ADMIN"));
		System.out.println("STATION JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_STATION"));
		System.out.println("PARKING JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_PARKING"));
		System.out.println("SERVICE JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_SERVICE"));
	}

}
