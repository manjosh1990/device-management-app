package com.manjosh.labs.device_service.model;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;

import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Device {
  @Id
  private String deviceId;

  private String name;

  private String ipAddress;

  private int port;

  private String vendor;

  private String model;

  private String status;

  private LocalDateTime createdAt;
}
