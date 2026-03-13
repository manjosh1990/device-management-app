package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.CommandStatus;
import com.manjosh.labs.devicecontracts.models.CommandStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CommandStatusClient implements CommandService {
  private final RestClient restClient;

  public void updateStatus(final Long commandId, final CommandStatus status) {
    final CommandStatusUpdateRequest request = new CommandStatusUpdateRequest(status);

    restClient
        .patch()
        .uri("/commands/{id}/status", commandId)
        .body(request)
        .retrieve()
        .toBodilessEntity();
  }
}
