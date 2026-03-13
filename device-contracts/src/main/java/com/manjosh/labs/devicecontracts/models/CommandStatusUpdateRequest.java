package com.manjosh.labs.devicecontracts.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandStatusUpdateRequest {

  private CommandStatus status;
}
