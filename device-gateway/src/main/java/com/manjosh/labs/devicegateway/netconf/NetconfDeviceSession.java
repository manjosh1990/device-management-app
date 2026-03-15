package com.manjosh.labs.devicegateway.netconf;

import com.manjosh.labs.devicegateway.exception.NetconfConnectionException;
import com.tailf.jnc.Element;
import com.tailf.jnc.JNCException;
import com.tailf.jnc.NetconfSession;
import com.tailf.jnc.NodeSet;
import com.tailf.jnc.SSHConnection;
import com.tailf.jnc.SSHSession;
import com.tailf.jnc.XMLParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NetconfDeviceSession {

  private static final int CONNECTION_TIMEOUT_MS = 10000;

  private final String deviceId;
  private final String host;
  private final int port;
  private final String username;
  private final String password;
  private final XMLParser parser;

  private boolean connected = false;

  private SSHSession sshSession;
  private NetconfSession netconfSession;

  public NetconfDeviceSession(
      final String deviceId,
      final String host,
      final int port,
      final String username,
      final String password) {
    this.deviceId = deviceId;
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.parser = createParser();
  }

  public synchronized void connect() {
    if (connected) {
      log.debug("NETCONF session already connected for device {}", deviceId);
      return;
    }

    log.info("Connecting to NETCONF device {} at {}:{}", deviceId, host, port);

    try {
      final SSHConnection connection = new SSHConnection(host, port, CONNECTION_TIMEOUT_MS);
      connection.authenticateWithPassword(username, password);

      sshSession = new SSHSession(connection);
      netconfSession = new NetconfSession(sshSession);
      connected = true;

      log.info("NETCONF session established for device {}", deviceId);
    } catch (final Exception ex) {
      resetSessionState();
      log.error("Failed to connect to NETCONF device {}", deviceId, ex);
      throw new NetconfConnectionException("NETCONF connection failed for device " + deviceId, ex);
    }
  }

  public synchronized String getConfig() {
    ensureConnected();
    log.info("Getting running config from device {}", deviceId);

    try {
      final NodeSet config = netconfSession.getConfig(NetconfSession.RUNNING);
      return config.toXMLString();
    } catch (final Exception ex) {
      log.error("get-config failed for device {}", deviceId, ex);
      resetSessionState();
      throw new NetconfConnectionException("get-config failed for device " + deviceId, ex);
    }
  }

  public synchronized String executeRpc(final String rpc) {
    ensureConnected();
    log.info("Executing RPC on device {}", deviceId);
    log.debug("RPC payload for device {}: {}", deviceId, rpc);

    try {
      final Element rpcElement = parser.parse(rpc);
      final Element reply = netconfSession.rpc(rpcElement);
      return reply.toXMLString();
    } catch (final JNCException ex) {
      log.error("RPC execution failed for device {}", deviceId, ex);
      resetSessionState();
      throw new NetconfConnectionException("NETCONF RPC failed for device " + deviceId, ex);
    } catch (final Exception ex) {
      log.error("Unexpected RPC execution failure for device {}", deviceId, ex);
      resetSessionState();
      throw new NetconfConnectionException("Unexpected NETCONF failure for device " + deviceId, ex);
    }
  }

  public synchronized void close() {
    log.info("Closing NETCONF session for device {}", deviceId);
    resetSessionState();
  }

  private void ensureConnected() {
    if (!connected) {
      connect();
    }
  }

  private XMLParser createParser() {
    try {
      return new XMLParser();
    } catch (final JNCException ex) {
      throw new NetconfConnectionException(
          "Failed to initialize XML parser for device " + deviceId, ex);
    }
  }

  private void resetSessionState() {
    connected = false;
    netconfSession = null;
    sshSession = null;
  }
}
