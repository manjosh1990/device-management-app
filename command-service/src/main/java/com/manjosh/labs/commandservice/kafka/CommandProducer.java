package com.manjosh.labs.commandservice.kafka;

import com.manjosh.labs.commandservice.config.KafkaProperties;
import com.manjosh.labs.commandservice.event.CommandEvent;
import com.manjosh.labs.commandservice.exception.KafkaSerializationException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CommandProducer {

  private final KafkaProperties kafkaProperties;
  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void publishCommand(final CommandEvent event) {

    try {
      final byte[] payload = objectMapper.writeValueAsBytes(event);

      kafkaTemplate.send(
          kafkaProperties.getTopic().getDeviceCommands(), event.getDeviceId(), payload);

    } catch (final Exception e) {
      throw new KafkaSerializationException("Failed to serialize command event", e);
    }
  }
}
