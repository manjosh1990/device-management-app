package com.manjosh.labs.devicegateway.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjosh.labs.devicecontracts.models.CommandEvent;
import com.manjosh.labs.devicegateway.service.DeviceCommandScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandConsumer {

  private final DeviceCommandScheduler scheduler;
  private final ObjectMapper objectMapper;

  @KafkaListener(
      topics = "#{@kafkaProperties.topic}",
      groupId = "#{@kafkaProperties.groupId}",
      containerFactory = "kafkaListenerContainerFactory")
  public void consume(final ConsumerRecord<String, byte[]> record, final Acknowledgment ack)
      throws Exception {
    try {
      log.info(
          "Raw Kafka message received. key={}, partition={}, offset={}, payloadSize={}",
          record.key(),
          record.partition(),
          record.offset(),
          record.value() == null ? 0 : record.value().length);

      final CommandEvent command = objectMapper.readValue(record.value(), CommandEvent.class);

      log.info("Received command {}", command);

      scheduler.schedule(command);

      ack.acknowledge();
    } catch (final Exception e) {
      log.error(
          "Failed to process Kafka record. key={}, partition={}, offset={}",
          record.key(),
          record.partition(),
          record.offset(),
          e);
      throw e;
    }
  }
}
