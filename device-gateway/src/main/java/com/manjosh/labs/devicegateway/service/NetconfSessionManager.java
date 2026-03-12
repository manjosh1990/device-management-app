package com.manjosh.labs.devicegateway.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NetconfSessionManager {
  private final Map<String, NetconfSession> sessions = new ConcurrentHashMap<>();

  public NetconfSession getSession(String deviceId) {

    return sessions.computeIfAbsent(deviceId, this::createSession);
  }

  private NetconfSession createSession(final String deviceId) {

    log.info("Creating NETCONF session for device {}", deviceId);

    // later we open SSH + NETCONF here
    return new NetconfSession(deviceId);
  }

  public void closeSession(final String deviceId) {

    NetconfSession session = sessions.remove(deviceId);

    if (session != null) {
      session.close();
    }
  }
}
