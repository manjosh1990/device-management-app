package com.manjosh.labs.commandservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaProperties {

  private TopicProperties topic;

  @Data
  public static class TopicProperties {
    private String deviceCommands;
  }
}
