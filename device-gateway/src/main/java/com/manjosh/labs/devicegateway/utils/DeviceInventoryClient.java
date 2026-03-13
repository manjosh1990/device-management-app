package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.DeviceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceInventoryClient implements DeviceInventoryService {

  private final RestClient deviceServiceRestClient;

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
