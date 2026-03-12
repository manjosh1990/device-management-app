package com.manjosh.labs.commandservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandEvent {
  private Long commandId;
  private String deviceId;
  private String commandType;
  private String payload;
}
