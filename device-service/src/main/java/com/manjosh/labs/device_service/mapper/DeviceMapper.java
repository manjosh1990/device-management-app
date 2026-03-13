package com.manjosh.labs.device_service.mapper;

import com.manjosh.labs.device_service.dto.DeviceRequest;
import com.manjosh.labs.device_service.dto.DeviceResponse;
import com.manjosh.labs.device_service.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

  public Device toEntity(final DeviceRequest request) {
    final Device device = new Device();
    device.setDeviceId(request.getDeviceId());
    device.setName(request.getName());
    device.setIpAddress(request.getIpAddress());
    device.setPort(request.getPort());
    device.setVendor(request.getVendor());
    device.setModel(request.getModel());
    return device;
  }

  public DeviceResponse toResponse(final Device device) {
    return DeviceResponse.builder()
        .deviceId(device.getDeviceId())
        .name(device.getName())
        .ipAddress(device.getIpAddress())
        .port(device.getPort())
        .vendor(device.getVendor())
        .model(device.getModel())
        .status(device.getStatus())
        .build();
  }
}
