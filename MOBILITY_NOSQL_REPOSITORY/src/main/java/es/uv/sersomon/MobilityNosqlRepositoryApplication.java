package es.uv.sersomon;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.uv.sersomon.enums.Operations;
import es.uv.sersomon.models.Event;
import es.uv.sersomon.repositories.EventRepository;
import es.uv.sersomon.services.EventService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Mobility NoSQL API", version = "v1", contact = @Contact(name = "Sergi Solera Monforte", email = "sersomon@alumni.uv.es"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), description = "Repository for aggregated Data on NoSQL"), servers = @Server(url = "/", description = "Develop"))
public class MobilityNosqlRepositoryApplication {

	@Autowired
	EventService eventService;

	@Autowired
	EventRepository eventRepository;

	private final static Logger LOGGER = LoggerFactory.getLogger(MobilityNosqlRepositoryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MobilityNosqlRepositoryApplication.class, args);
	}

}
