package com.manjosh.labs.devicegateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaProperties {

  private String bootstrapServers;
  private String groupId;
  private String autoOffsetReset;
  private String topic;
  private int concurrency = 3;
  private String ackMode = "MANUAL";
}
