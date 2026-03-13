package com.manjosh.labs.devicecontracts.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandResponse {

  private Long id;

  private String deviceId;

  private CommandType commandType;

  private CommandStatus status;

  private String payload;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
