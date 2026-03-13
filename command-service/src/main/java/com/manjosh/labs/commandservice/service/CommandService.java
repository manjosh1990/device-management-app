package com.manjosh.labs.commandservice.service;

import com.manjosh.labs.commandservice.dto.CommandRequest;
import com.manjosh.labs.commandservice.exception.CommandNotFoundException;
import com.manjosh.labs.commandservice.kafka.CommandProducer;
import com.manjosh.labs.commandservice.mapper.CommandMapper;
import com.manjosh.labs.commandservice.model.Command;
import com.manjosh.labs.commandservice.repository.CommandRepository;
import com.manjosh.labs.devicecontracts.models.CommandEvent;
import com.manjosh.labs.devicecontracts.models.CommandResponse;
import com.manjosh.labs.devicecontracts.models.CommandStatus;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandService {

  private final CommandRepository repository;
  private final CommandProducer producer;
  private final CommandMapper commandMapper;

  public CommandResponse createCommand(final CommandRequest request) {

    final Command command = commandMapper.toEntity(request);
    final Command saved = repository.save(command);

    final CommandEvent event = commandMapper.toEvent(saved);
    producer.publishCommand(event);

    return commandMapper.toResponse(saved);
  }

  @Transactional
  public void updateStatus(final Long commandId, final CommandStatus status) {

    final Command command =
        repository
            .findById(commandId)
            .orElseThrow(() -> new CommandNotFoundException("Command not found"));

    command.setStatus(status);
    command.setUpdatedAt(LocalDateTime.now());

    repository.save(command);
  }

  public CommandResponse getCommand(final Long id) {

    final Command command =
        repository
            .findById(id)
            .orElseThrow(() -> new CommandNotFoundException("Command not found"));

    return commandMapper.toResponse(command);
  }
}
