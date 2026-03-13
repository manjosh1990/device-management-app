package com.manjosh.labs.devicegateway.service;

import static com.manjosh.labs.devicecontracts.models.CommandStatus.FAILED;
import static com.manjosh.labs.devicecontracts.models.CommandStatus.IN_PROGRESS;
import static com.manjosh.labs.devicecontracts.models.CommandStatus.SUCCESS;

import com.manjosh.labs.devicecontracts.models.CommandEvent;
import com.manjosh.labs.devicegateway.netconf.NetconfService;
import com.manjosh.labs.devicegateway.utils.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandExecutor {

  private final NetconfService netconfService;

  private final CommandService commandService;

  public void execute(final CommandEvent command) {

    Long commandId = command.getCommandId();
    String deviceId = command.getDeviceId();

    try {

      log.info("Starting execution for command {}", commandId);

      // mark IN_PROGRESS
      commandService.updateStatus(commandId, IN_PROGRESS);

      String response = netconfService.executeCommand(deviceId, command.getPayload());

      log.info("Device response: {}", response);

      // mark SUCCESS
      commandService.updateStatus(commandId, SUCCESS);

    } catch (Exception ex) {

      log.error("Command execution failed for {}", commandId, ex);

      commandService.updateStatus(commandId, FAILED);
    }
  }
}
