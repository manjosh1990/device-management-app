package com.manjosh.labs.devicegateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.clients.command-service")
public class CommandServiceClientProperties {

  private String baseUrl;
}
