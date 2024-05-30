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

import es.uv.sersomon.models.Aggregate;
import es.uv.sersomon.repositories.AggregateRepository;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(AggregateRepository aggregateRepository) {
        return args -> {
            // Leer el archivo JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            TypeReference<List<Aggregate>> typeReference = new TypeReference<List<Aggregate>>() {
            };
            try {
                InputStream inputStream = getClass().getResourceAsStream("/data.json");

                List<Aggregate> measurements = mapper.readValue(inputStream, typeReference);
                if (aggregateRepository.count() == 0) {
                    aggregateRepository.saveAll(measurements);
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
