package manga_up.manga_up.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
 

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("https://manga-up.onrender.com/")))
                .info(new Info()
                        .title("MangaUp API")
                        .version("1.0")
                        .description("Documentation MangaUp API"));
    }

}
