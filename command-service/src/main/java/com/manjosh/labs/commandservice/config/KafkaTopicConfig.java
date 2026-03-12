package com.manjosh.labs.commandservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public NewTopic deviceCommandsTopic() {
    return TopicBuilder.name(kafkaProperties.getTopic().getDeviceCommands())
        .partitions(6)
        .replicas(1)
        .build();
  }
}
