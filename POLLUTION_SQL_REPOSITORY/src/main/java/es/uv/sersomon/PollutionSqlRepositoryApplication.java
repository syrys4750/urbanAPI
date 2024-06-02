package es.uv.sersomon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Pollution SQL API", version = "v1", contact = @Contact(name = "Sergi Solera Monforte", email = "sersomon@alumni.uv.es"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), description = "Repository for aggregated Data on NoSQL"), servers = @Server(url = "/", description = "Develop"))
public class PollutionSqlRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollutionSqlRepositoryApplication.class, args);
	}

}
