package es.uv.sersomon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.models.Parking;
import es.uv.sersomon.services.ParkingService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Mobility SQL API", version = "v1", contact = @Contact(name = "Sergi Solera Monforte", email = "sersomon@alumni.uv.es"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), description = "Repository for aggregated Data on NoSQL"), servers = @Server(url = "/", description = "Develop"))
public class MobilitySqlRepositoryApplication {

	@Autowired
	ParkingService parkingService;

	public static void main(String[] args) {
		SpringApplication.run(MobilitySqlRepositoryApplication.class, args);
	}

}
