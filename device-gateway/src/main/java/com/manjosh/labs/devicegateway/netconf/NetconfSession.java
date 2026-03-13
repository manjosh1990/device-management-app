package com.manjosh.labs.devicegateway.netconf;

import com.manjosh.labs.devicegateway.exception.NetconfConnectionException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
public class NetconfSession {
  private final String deviceId;
  private final String host;
  private final int port;
  private boolean connected = false;

  public void connect() {

    log.info("Connecting to NETCONF device {} at {}:{}", deviceId, host, port);

    try {
      // TODO: replace with real NETCONF SSH connection
      connected = true;

      log.info("NETCONF session established for {}", deviceId);

    } catch (Exception e) {
      log.error("Failed to connect to device {}", deviceId, e);
      throw new NetconfConnectionException("NETCONF connection failed", e);
    }
  }

  public String executeRpc(String rpc) {

    if (!connected) {
      throw new IllegalStateException("Session not connected");
    }

    log.info("Executing RPC on device {} : {}", deviceId, rpc);

    // mock response for now
    return "<rpc-reply>ok</rpc-reply>";
  }

  public void close() {

    log.info("Closing NETCONF session for {}", deviceId);

    connected = false;
  }
}
