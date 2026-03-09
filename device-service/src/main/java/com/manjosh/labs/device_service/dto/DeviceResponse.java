package com.manjosh.labs.device_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceResponse {
  private String deviceId;
  private String name;
  private String ipAddress;
  private String vendor;
  private String model;
  private String status;
}
