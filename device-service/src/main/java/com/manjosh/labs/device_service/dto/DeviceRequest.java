package com.manjosh.labs.device_service.dto;

import lombok.Data;

@Data
public class DeviceRequest {
  private String deviceId;
  private String name;
  private String ipAddress;
  private int port;
  private String vendor;
  private String model;
}
