package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.services.JwtService;

@SpringBootApplication
public class PollutionApiApplication implements CommandLineRunner {
	@Autowired
	JwtService jwtService;

	public static void main(String[] args) {
		SpringApplication.run(PollutionApiApplication.class, args);
		System.out.println("JWT TOKEN FOR ADMIN: ");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("ADMIN JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_ADMIN"));
		System.out.println("STATION JWT TOKEN: " + jwtService.generateAccessToken(1, "ROLE_STATION"));
	}
}
