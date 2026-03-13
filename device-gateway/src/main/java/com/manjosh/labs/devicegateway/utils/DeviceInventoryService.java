package com.manjosh.labs.devicegateway.utils;

import com.manjosh.labs.devicecontracts.models.DeviceResponse;

public interface DeviceInventoryService {
  DeviceResponse getDevice(String deviceId);
}
