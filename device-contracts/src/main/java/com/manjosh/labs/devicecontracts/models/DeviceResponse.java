package com.manjosh.labs.devicecontracts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
  private String deviceId;
  private String name;
  private String ipAddress;
  private int port;
  private String vendor;
  private String model;
  private DeviceStatus status;
}
