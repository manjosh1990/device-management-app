package com.manjosh.labs.commandservice.mapper;

import com.manjosh.labs.commandservice.dto.CommandRequest;
import com.manjosh.labs.commandservice.model.Command;
import com.manjosh.labs.devicecontracts.models.CommandEvent;
import com.manjosh.labs.devicecontracts.models.CommandResponse;
import com.manjosh.labs.devicecontracts.models.CommandStatus;
import com.manjosh.labs.devicecontracts.models.CommandType;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CommandMapper {

  public Command toEntity(final CommandRequest request) {
    return Command.builder()
        .deviceId(request.getDeviceId())
        .commandType(CommandType.valueOf(request.getCommandType()))
        .payload(request.getPayload())
        .status(CommandStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public CommandEvent toEvent(final Command command) {
    return CommandEvent.builder()
        .commandId(command.getId())
        .deviceId(command.getDeviceId())
        .commandType(command.getCommandType().name())
        .payload(command.getPayload())
        .build();
  }

  public CommandResponse toResponse(final Command command) {
    return CommandResponse.builder()
        .id(command.getId())
        .deviceId(command.getDeviceId())
        .commandType(command.getCommandType())
        .status(command.getStatus())
        .payload(command.getPayload())
        .createdAt(command.getCreatedAt())
        .updatedAt(command.getUpdatedAt())
        .build();
  }
}
