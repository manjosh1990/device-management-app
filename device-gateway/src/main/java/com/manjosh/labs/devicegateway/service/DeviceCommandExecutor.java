package com.manjosh.labs.devicegateway.service;

import com.manjosh.labs.devicecontracts.models.CommandEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceCommandExecutor {
  private final NetconfSessionManager sessionManager;

  public void execute(final CommandEvent command) {

    log.info("Executing command {} on device {}", command.getCommandType(), command.getDeviceId());
    NetconfSession session = sessionManager.getSession(command.getDeviceId());

    final String result = session.executeRpc(command.getCommandType(), command.getPayload());

    log.info("Device response {}", result);
  }
}
