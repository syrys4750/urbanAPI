package es.uv.sersomon.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.uv.sersomon.models.Measurement;
import es.uv.sersomon.repositories.MeasurementRepository;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(MeasurementRepository measurementRepository) {
        return args -> {
            // Leer el archivo JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            TypeReference<List<Measurement>> typeReference = new TypeReference<List<Measurement>>() {
            };
            InputStream inputStream = getClass().getResourceAsStream("/data.json");
            try {
                List<Measurement> measurements = mapper.readValue(inputStream, typeReference);
                if (measurementRepository.count() == 0) {
                    measurementRepository.saveAll(measurements);
                    System.out.println("Initial data loaded.");
                } else {
                    System.out.println("No initial data required. Events already present on database.");
                }

            } catch (IOException e) {
                System.out.println("No se pudo cargar el archivo JSON: " + e.getMessage());
            }
        };
    }
}
