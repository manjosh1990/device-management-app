package com.manjosh.labs.devicegateway.netconf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NetconfService {

  private final NetconfSessionManager sessionManager;

  public String executeCommand(final String deviceId, final String commandType, final String payload) {
    log.info("Executing NETCONF command {} for device {}", commandType, deviceId);

    final NetconfDeviceSession session = sessionManager.getSession(deviceId);

    return switch (commandType) {
      case "GET_CONFIG" -> session.getConfig();
      default -> session.executeRpc(payload);
    };
  }
}
