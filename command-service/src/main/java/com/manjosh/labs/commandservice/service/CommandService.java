package com.manjosh.labs.commandservice.service;

import com.manjosh.labs.commandservice.dto.CommandRequest;
import com.manjosh.labs.commandservice.kafka.CommandProducer;
import com.manjosh.labs.commandservice.model.Command;
import com.manjosh.labs.commandservice.repository.CommandRepository;
import com.manjosh.labs.devicecontracts.models.CommandEvent;
import com.manjosh.labs.devicecontracts.models.CommandStatus;
import com.manjosh.labs.devicecontracts.models.CommandType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandService {

  private final CommandRepository repository;
  private final CommandProducer producer;

  public Command createCommand(final CommandRequest request) {

    final Command command =
        Command.builder()
            .deviceId(request.getDeviceId())
            .commandType(CommandType.valueOf(request.getCommandType()))
            .payload(request.getPayload())
            .status(CommandStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();

    final Command saved = repository.save(command);

    final CommandEvent event =
        CommandEvent.builder()
            .commandId(saved.getId())
            .deviceId(saved.getDeviceId())
            .commandType(saved.getCommandType().name())
            .payload(saved.getPayload())
            .build();

    producer.publishCommand(event);

    return saved;
  }
}
