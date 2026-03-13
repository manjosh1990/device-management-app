package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.CommandStatus;
import com.manjosh.labs.devicecontracts.models.CommandStatusUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class CommandStatusClient implements CommandService {

  private final RestClient restClient;

  public CommandStatusClient(@Qualifier("commandServiceClient") final RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void updateStatus(final Long commandId, final CommandStatus status) {
    final CommandStatusUpdateRequest request = new CommandStatusUpdateRequest(status);

    log.info("Updating command status. commandId={}, status={}", commandId, status);

    restClient
        .patch()
        .uri("/commands/{id}/status", commandId)
        .body(request)
        .retrieve()
        .toBodilessEntity();
  }
}
