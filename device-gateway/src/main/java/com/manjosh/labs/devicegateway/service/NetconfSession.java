package com.manjosh.labs.devicegateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NetconfSession {
  private final String deviceId;

  public String executeRpc(final String command,final String payload) {

    log.info("Sending NETCONF RPC to {} command={}", deviceId, command);

    // later we call real NETCONF here

    return "<rpc-reply>ok</rpc-reply>";
  }

  public void close() {

    log.info("Closing session for {}", deviceId);
  }
}
