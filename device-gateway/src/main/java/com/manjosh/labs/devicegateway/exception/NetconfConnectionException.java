package com.manjosh.labs.devicegateway.exception;

public class NetconfConnectionException extends RuntimeException {
  public NetconfConnectionException(final String netconfConnectionFailed, final Exception e) {
    super(netconfConnectionFailed, e);
  }
}
