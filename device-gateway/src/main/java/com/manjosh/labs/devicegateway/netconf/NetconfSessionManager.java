package com.manjosh.labs.devicegateway.netconf;

import com.manjosh.labs.devicecontracts.models.DeviceResponse;
import com.manjosh.labs.devicegateway.config.NetconfProperties;
import com.manjosh.labs.devicegateway.utils.DeviceInventoryService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetconfSessionManager {

  private final DeviceInventoryService deviceInventoryService;
  private final NetconfProperties netconfProperties;

  private final Map<String, NetconfDeviceSession> sessions = new ConcurrentHashMap<>();

  public NetconfDeviceSession getSession(final String deviceId) {
    return sessions.computeIfAbsent(deviceId, this::createSession);
  }

  private NetconfDeviceSession createSession(final String deviceId) {
    log.info("Creating NETCONF session for {}", deviceId);

    final DeviceResponse device = deviceInventoryService.getDevice(deviceId);

    final NetconfDeviceSession session =
        new NetconfDeviceSession(
            device.getDeviceId(),
            device.getIpAddress(),
            device.getPort(),
            netconfProperties.getUsername(),
            netconfProperties.getPassword());

    session.connect();
    return session;
  }

  public void closeSession(final String deviceId) {
    final NetconfDeviceSession session = sessions.remove(deviceId);

    if (session != null) {
      session.close();
    }
  }
}
