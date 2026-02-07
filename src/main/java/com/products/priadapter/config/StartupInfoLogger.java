package com.products.priadapter.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Componente que muestra información útil al iniciar la aplicación.
 * Incluye URLs de Swagger, API Docs y Health Check.
 *
 * <p>Solo se muestra en perfiles de desarrollo (default, dev, local, test).
 * En producción no se muestra para evitar ruido en logs.
 *
 * <p>Todas las URLs se construyen dinámicamente desde las propiedades de configuración.
 */
@Slf4j
@Component
public class StartupInfoLogger {

  private static final String SEPARATOR = "════════════════════════════════════════════════════════════════";

  private final Environment environment;

  @Value("${server.port:8080}")
  private String port;

  @Value("${server.address:localhost}")
  private String host;

  @Value("${server.ssl.enabled:false}")
  private boolean sslEnabled;

  @Value("${server.servlet.context-path:#{null}}")
  private String contextPath;

  @Value("${springdoc.swagger-ui.path:#{'/swagger-ui.html'}}")
  private String swaggerPath;

  @Value("${springdoc.api-docs.path:#{'/v3/api-docs'}}")
  private String apiDocsPath;

  @Value("${management.endpoints.web.base-path:#{'/actuator'}}")
  private String actuatorBasePath;

  public StartupInfoLogger(Environment environment) {
    this.environment = environment;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    if (isProductionProfile()) {
      return;
    }

    String baseUrl = buildBaseUrl();

    log.info(SEPARATOR);
    log.info("                    PRODUCTS API - STARTED                       ");
    log.info(SEPARATOR);
    log.info("  Swagger UI:     {}{}", baseUrl, swaggerPath);
    log.info("  API Docs:       {}{}", baseUrl, apiDocsPath);
    log.info("  Health Check:   {}{}/health", baseUrl, actuatorBasePath);
    log.info(SEPARATOR);
  }

  private String buildBaseUrl() {
    String protocol = sslEnabled ? "https" : "http";
    String path = contextPath != null ? contextPath : "";
    return protocol + "://" + host + ":" + port + path;
  }

  private boolean isProductionProfile() {
    String[] activeProfiles = environment.getActiveProfiles();
    return Arrays.stream(activeProfiles)
        .anyMatch(profile -> profile.equalsIgnoreCase("prod") || profile.equalsIgnoreCase("production"));
  }

}
