package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.DeviceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class DeviceInventoryClient implements DeviceInventoryService {

  private final RestClient deviceServiceRestClient;

  public DeviceInventoryClient(
      @Qualifier("deviceServiceClient") final RestClient deviceServiceRestClient) {
    this.deviceServiceRestClient = deviceServiceRestClient;
  }

  @Override
  public DeviceResponse getDevice(final String deviceId) {
    log.info("Fetching device info for deviceId={}", deviceId);

    return deviceServiceRestClient
        .get()
        .uri("/devices/{id}", deviceId)
        .retrieve()
        .body(DeviceResponse.class);
  }
}
