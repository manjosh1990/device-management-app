package com.manjosh.labs.device_service.model;

import com.manjosh.labs.devicecontracts.models.DeviceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Device {
  @Id private String deviceId;

  private String name;

  private String ipAddress;

  private int port;

  private String vendor;

  private String model;

  @Enumerated(EnumType.STRING)
  private DeviceStatus status;

  private LocalDateTime createdAt;
}
