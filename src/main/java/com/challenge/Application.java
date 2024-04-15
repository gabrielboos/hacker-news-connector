package com.challenge;

import com.challenge.config.JwtConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfiguration.class)
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(title = "Hacker News connector", version = "1.0", description = "The connector and indexer of hacker news articles.")
)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
