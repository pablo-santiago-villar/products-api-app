package com.products.priadapter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .addServersItem(new Server()
            .url("http://localhost:8080")
            .description("Servidor Local"))
        .addServersItem(new Server()
            .url("https://api.productsapi.com")
            .description("Servidor Producción"))
        .info(new Info()
            .title("Products API")
            .version("1.0.0")
            .description(
                "API REST para gestión y búsqueda de productos con filtrado temporal y "
                    + "validación de disponibilidad por marca"));

  }
}
