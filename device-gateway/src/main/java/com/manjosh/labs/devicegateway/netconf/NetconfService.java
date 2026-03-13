package com.manjosh.labs.devicegateway.netconf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NetconfService {

  private final NetconfSessionManager sessionManager;

  public String executeCommand(String deviceId, String rpc) {

    log.info("Executing NETCONF command for device {}", deviceId);

    NetconfSession session = sessionManager.getSession(deviceId);

    return session.executeRpc(rpc);
  }
}
