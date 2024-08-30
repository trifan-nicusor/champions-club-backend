package ro.championsclub.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Champions Club API",
                description = "Gym management API built with Spring Boot 3.3.x",
                version = "1.0.0"
        ),
        servers = @Server(
                url = "/", description = "Dev Server"
        )
)
public class OpenApiConfig {

}
