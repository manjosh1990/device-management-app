package com.manjosh.labs.devicegateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.clients.device-service")
@Component
@Data
public class DeviceServiceProperties {
  private String baseUrl;
}
