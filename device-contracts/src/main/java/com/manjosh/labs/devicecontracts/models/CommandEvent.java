package com.manjosh.labs.devicecontracts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandEvent {

  private Long commandId;
  private String deviceId;
  private String commandType;
  private String payload;
}
