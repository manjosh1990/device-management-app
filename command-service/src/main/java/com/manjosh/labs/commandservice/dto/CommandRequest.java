package com.manjosh.labs.commandservice.dto;

import lombok.Data;

@Data
public class CommandRequest {
  private String deviceId;
  private String commandType;
  private String payload;
}
