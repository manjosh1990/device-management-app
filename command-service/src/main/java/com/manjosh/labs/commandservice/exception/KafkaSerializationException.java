package com.manjosh.labs.commandservice.exception;

public class KafkaSerializationException extends RuntimeException {

  public KafkaSerializationException(final String message) {
    super(message);
  }

  public KafkaSerializationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
