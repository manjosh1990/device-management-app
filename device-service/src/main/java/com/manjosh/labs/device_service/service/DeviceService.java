package com.manjosh.labs.device_service.service;

import com.manjosh.labs.device_service.dto.DeviceRequest;
import com.manjosh.labs.device_service.dto.DeviceResponse;
import com.manjosh.labs.device_service.exception.DeviceNotFoundException;
import com.manjosh.labs.device_service.model.Device;
import com.manjosh.labs.device_service.repository.DeviceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

  @Autowired private DeviceRepository repository;

  public DeviceResponse register(DeviceRequest request) {

    Device device = new Device();
    device.setDeviceId(request.getDeviceId());
    device.setName(request.getName());
    device.setIpAddress(request.getIpAddress());
    device.setPort(request.getPort());
    device.setVendor(request.getVendor());
    device.setModel(request.getModel());
    device.setStatus("REGISTERED");
    device.setCreatedAt(LocalDateTime.now());

    repository.save(device);

    return mapToResponse(device);
  }

  public List<DeviceResponse> getAllDevices() {

    return repository.findAll().stream().map(this::mapToResponse).toList();
  }

  public DeviceResponse getDevice(String deviceId) {

    Device device =
        repository
            .findById(deviceId)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

    return mapToResponse(device);
  }

  private DeviceResponse mapToResponse(Device device) {

    return DeviceResponse.builder()
        .deviceId(device.getDeviceId())
        .name(device.getName())
        .ipAddress(device.getIpAddress())
        .vendor(device.getVendor())
        .model(device.getModel())
        .status(device.getStatus())
        .build();
  }
}
