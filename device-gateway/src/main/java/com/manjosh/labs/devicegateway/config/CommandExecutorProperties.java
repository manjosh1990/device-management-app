package com.manjosh.labs.devicegateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.command-executor")
public class CommandExecutorProperties {

  private int corePoolSize = 10;
  private int maxPoolSize = 10;
  private int queueCapacity = 200;
  private int awaitTerminationSeconds = 10;
}
