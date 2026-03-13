package com.manjosh.labs.devicegateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private final CommandServiceClientProperties commandServiceClientProperties;
  private final DeviceServiceProperties deviceServiceClientProperties;

  @Bean
  public RestClient commandServiceClient() {
    return RestClient.builder().baseUrl(commandServiceClientProperties.getBaseUrl()).build();
  }

  @Bean
  public RestClient deviceServiceClient() {
    return RestClient.builder().baseUrl(deviceServiceClientProperties.getBaseUrl()).build();
  }
}
