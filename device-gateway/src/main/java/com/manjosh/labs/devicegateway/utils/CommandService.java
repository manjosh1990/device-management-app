package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.CommandStatus;

public interface CommandService {
  void updateStatus(final Long commandId, final CommandStatus status);
}
