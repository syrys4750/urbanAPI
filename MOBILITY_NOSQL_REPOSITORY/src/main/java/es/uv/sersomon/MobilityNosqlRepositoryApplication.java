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

@SpringBootApplication
public class MobilityNosqlRepositoryApplication implements CommandLineRunner {

	@Autowired
	EventService eventService;

	@Autowired
	EventRepository eventRepository;

	private final static Logger LOGGER = LoggerFactory.getLogger(MobilityNosqlRepositoryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MobilityNosqlRepositoryApplication.class, args);
	}

	// TODO: es una forma válida de añadir datos o tengo que usar el fichero?
	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Creating events");
		eventService.createEvent(new Event("6453-ydydy-123636-132", 2, Operations.PARKING.toString(), 4, 2,
				LocalDate
						.ofInstant(Instant.ofEpochMilli(1715859130),
								TimeZone.getDefault().toZoneId())
						.atStartOfDay()));

	}

}
