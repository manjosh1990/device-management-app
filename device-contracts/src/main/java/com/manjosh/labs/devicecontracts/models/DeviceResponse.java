package com.manjosh.labs.devicecontracts.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceResponse {
  private String deviceId;
  private String name;
  private String ipAddress;
  private int port;
  private String vendor;
  private String model;
  private DeviceStatus status;
}
