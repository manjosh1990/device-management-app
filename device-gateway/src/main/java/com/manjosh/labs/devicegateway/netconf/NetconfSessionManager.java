package com.manjosh.labs.devicegateway.netconf;

import com.manjosh.labs.devicecontracts.models.DeviceResponse;
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

  private final Map<String, NetconfSession> sessions = new ConcurrentHashMap<>();

  public NetconfSession getSession(String deviceId) {

    return sessions.computeIfAbsent(deviceId, this::createSession);
  }

  private NetconfSession createSession(final String deviceId) {

    log.info("Creating NETCONF session for {}", deviceId);

    DeviceResponse device = deviceInventoryService.getDevice(deviceId);

    NetconfSession session =
        new NetconfSession(device.getDeviceId(), device.getIpAddress(), device.getPort());

    session.connect();
    return session;
  }

  public void closeSession(final String deviceId) {

    NetconfSession session = sessions.remove(deviceId);

    if (session != null) {
      session.close();
    }
  }
}
