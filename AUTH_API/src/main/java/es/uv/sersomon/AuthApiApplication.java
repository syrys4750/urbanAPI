package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.service.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "v1", contact = @Contact(name = "Sergi Solera Monforte", email = "sersomon@alumni.uv.es"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), description = "Repository for aggregated Data on NoSQL"), servers = @Server(url = "/", description = "Develop"))
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
